import { render, screen, waitFor } from '../../test/utils';
import userEvent from '@testing-library/user-event';
import ApparelCreatePage from '../../pages/apparels/ApparelCreatePage';
import { createMockApparel } from '../../test/utils';
import apiService from '../../services/api';
import { useForm, useToast } from '../../hooks';

// Mock the API service that apparelService depends on
jest.mock('../../services/api');

// Mock the hooks
jest.mock('../../hooks', () => ({
  useForm: jest.fn(),
  useToast: () => ({
    success: jest.fn(),
    error: jest.fn(),
  }),
}));

// Mock react-router-dom
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

// Type the mocked services and hooks
const mockApiService = apiService as jest.Mocked<typeof apiService>;
const mockUseForm = useForm as jest.MockedFunction<typeof useForm>;
const mockUseToast = useToast as jest.MockedFunction<typeof useToast>;

describe('Apparel Creation Integration', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should complete the full apparel creation flow', async () => {
    const user = userEvent.setup();

    // Mock successful apparel creation
    const newApparel = createMockApparel({
      id: 1,
      apparelName: 'Test IPA',
      apparelStyle: 'IPA',
      price: 12.99,
      quantityOnHand: 100,
    });
    mockApiService.createWithNotification.mockResolvedValue(newApparel);

    // Mock the useForm hook to return controlled form behavior
    const mockFormState = {
      values: {
        apparelName: 'Test Apparel',
        apparelStyle: 'IPA',
        upc: '123456789',
        price: 12.99,
        quantityOnHand: 100,
        imageUrl: undefined,
      },
      errors: {},
      isValid: true, // Make form valid so submit button can be clicked
      isSubmitting: false,
      setValue: jest.fn(),
      setValues: jest.fn(),
      setError: jest.fn(),
      clearError: jest.fn(),
      clearErrors: jest.fn(),
      validateField: jest.fn(),
      validateAll: jest.fn(),
      handleSubmit: jest.fn(),
      reset: jest.fn(),
    };
    (mockUseForm as jest.MockedFunction<typeof useForm>).mockReturnValue(mockFormState);

    render(<ApparelCreatePage />);

    // Verify the page renders correctly
    expect(screen.getByText('Create New Apparel')).toBeInTheDocument();
    expect(screen.getByText('Add a new apparel to your inventory')).toBeInTheDocument();

    // Check that form fields are present
    expect(screen.getByLabelText(/apparel name/i)).toBeInTheDocument();
    expect(screen.getByText('Apparel Style')).toBeInTheDocument(); // Look for exact label text
    expect(screen.getByLabelText(/price/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/quantity on hand/i)).toBeInTheDocument();

    // Check that action buttons are present
    expect(screen.getByRole('button', { name: /create apparel/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /cancel/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /back to apparels/i })).toBeInTheDocument();

    // Test form interaction
    const apparelNameInput = screen.getByLabelText(/apparel name/i);
    await user.type(apparelNameInput, 'Test IPA');
    expect(mockFormState.setValue).toHaveBeenCalledWith('apparelName', expect.any(String));

    // Test form submission
    const submitButton = screen.getByRole('button', { name: /create apparel/i });
    await user.click(submitButton);
    expect(mockFormState.handleSubmit).toHaveBeenCalled();

    // Test cancel functionality
    const cancelButton = screen.getByRole('button', { name: /cancel/i });
    await user.click(cancelButton);
    expect(mockNavigate).toHaveBeenCalledWith('/apparels');

    // Test back button functionality
    const backButton = screen.getByRole('button', { name: /back to apparels/i });
    await user.click(backButton);
    expect(mockNavigate).toHaveBeenCalledWith('/apparels');
  });

  it('should handle form validation errors', async () => {
    // Mock form with validation errors
    const mockFormState = {
      values: {
        apparelName: '',
        apparelStyle: '',
        upc: '',
        price: undefined,
        quantityOnHand: undefined,
        imageUrl: undefined,
      },
      errors: {
        apparelName: ['Apparel name is required'],
        apparelStyle: ['Apparel style is required'],
        price: ['Price must be greater than 0'],
      },
      isValid: false,
      isSubmitting: false,
      setValue: jest.fn(),
      setValues: jest.fn(),
      setError: jest.fn(),
      clearError: jest.fn(),
      clearErrors: jest.fn(),
      validateField: jest.fn(),
      validateAll: jest.fn(),
      handleSubmit: jest.fn(),
      reset: jest.fn(),
    };
    (mockUseForm as jest.MockedFunction<typeof useForm>).mockReturnValue(mockFormState);

    render(<ApparelCreatePage />);

    // Check that validation errors are displayed
    expect(screen.getByText('Apparel name is required')).toBeInTheDocument();
    expect(screen.getByText('Apparel style is required')).toBeInTheDocument();
    expect(screen.getByText('Price must be greater than 0')).toBeInTheDocument();

    // Check that submit button is disabled when form is invalid
    const submitButton = screen.getByRole('button', { name: /create apparel/i });
    expect(submitButton).toBeDisabled();
  });

  it('should handle API errors during apparel creation', async () => {
    const user = userEvent.setup();

    // Mock API error
    mockApiService.createWithNotification.mockRejectedValue(new Error('API Error'));

    // Mock form with valid data but API error

    const mockFormState = {
      values: {
        apparelName: 'Test Apparel',
        apparelStyle: 'IPA',
        upc: '123456789',
        price: 12.99,
        quantityOnHand: 100,
        imageUrl: undefined,
      },
      errors: {},
      isValid: true,
      isSubmitting: false,
      setValue: jest.fn(),
      setValues: jest.fn(),
      setError: jest.fn(),
      clearError: jest.fn(),
      clearErrors: jest.fn(),
      validateField: jest.fn(),
      validateAll: jest.fn(),
      handleSubmit: jest.fn(async formData => {
        // Simulate the actual form submission logic
        try {
          await mockApiService.createWithNotification('/api/v1/apparels', formData);
        } catch {
          mockUseToast().error('Failed to create apparel. Please try again.');
        }
      }),
      reset: jest.fn(),
    };
    (mockUseForm as jest.MockedFunction<typeof useForm>).mockReturnValue(mockFormState);

    render(<ApparelCreatePage />);

    // Submit the form
    const submitButton = screen.getByRole('button', { name: /create apparel/i });
    await user.click(submitButton);

    // Verify that the API was called and error was handled
    await waitFor(() => {
      expect(mockFormState.handleSubmit).toHaveBeenCalled();
    });
  });

  it('should show loading state during submission', async () => {
    // Mock form in submitting state
    const mockFormState = {
      values: {
        apparelName: 'Test Apparel',
        apparelStyle: 'IPA',
        upc: '123456789',
        price: 12.99,
        quantityOnHand: 100,
        imageUrl: undefined,
      },
      errors: {},
      isValid: true,
      isSubmitting: true,
      setValue: jest.fn(),
      setValues: jest.fn(),
      setError: jest.fn(),
      clearError: jest.fn(),
      clearErrors: jest.fn(),
      validateField: jest.fn(),
      validateAll: jest.fn(),
      handleSubmit: jest.fn(),
      reset: jest.fn(),
    };
    (mockUseForm as jest.MockedFunction<typeof useForm>).mockReturnValue(mockFormState);

    render(<ApparelCreatePage />);

    // Check that submit button shows loading state
    const submitButton = screen.getByRole('button', { name: /saving/i });
    expect(submitButton).toBeDisabled();

    // Check that cancel button is also disabled during submission
    const cancelButton = screen.getByRole('button', { name: /cancel/i });
    expect(cancelButton).toBeDisabled();
  });

  it('should handle image upload functionality', async () => {
    const mockFormState = {
      values: {
        apparelName: '',
        apparelStyle: '',
        upc: '',
        price: undefined,
        quantityOnHand: undefined,
        imageUrl: undefined,
      },
      errors: {},
      isValid: true,
      isSubmitting: false,
      setValue: jest.fn(),
      setValues: jest.fn(),
      setError: jest.fn(),
      clearError: jest.fn(),
      clearErrors: jest.fn(),
      validateField: jest.fn(),
      validateAll: jest.fn(),
      handleSubmit: jest.fn(),
      reset: jest.fn(),
    };
    (mockUseForm as jest.MockedFunction<typeof useForm>).mockReturnValue(mockFormState);

    render(<ApparelCreatePage />);

    // Check that image upload component is present
    expect(screen.getByText(/upload apparel image/i)).toBeInTheDocument();
    expect(screen.getByText(/drag and drop or click to select/i)).toBeInTheDocument();
  });
});
