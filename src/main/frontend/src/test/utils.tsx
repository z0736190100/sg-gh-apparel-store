import type { ReactElement } from 'react';
import { render } from '@testing-library/react';
import type { RenderOptions } from '@testing-library/react';
import { AllTheProviders } from './TestProviders';

const customRender = (ui: ReactElement, options?: Omit<RenderOptions, 'wrapper'>) =>
  render(ui, { wrapper: AllTheProviders, ...options });

// Re-export commonly used testing utilities
export {
  screen,
  fireEvent,
  waitFor,
  waitForElementToBeRemoved,
  within,
  getByRole,
  getByText,
  getByLabelText,
  getByPlaceholderText,
  getByTestId,
  queryByRole,
  queryByText,
  queryByLabelText,
  queryByPlaceholderText,
  queryByTestId,
  findByRole,
  findByText,
  findByLabelText,
  findByPlaceholderText,
  findByTestId,
  act,
  cleanup,
  renderHook,
} from '@testing-library/react';
export { customRender as render };

// Custom matchers and utilities
export const waitForLoadingToFinish = () => {
  return new Promise(resolve => setTimeout(resolve, 0));
};

// Mock data factories
export const createMockApparel = (overrides = {}) => ({
  id: 1,
  version: 1,
  createdDate: '2023-01-01T00:00:00Z',
  updateDate: '2023-01-01T00:00:00Z',
  apparelName: 'Test Apparel',
  apparelStyle: 'Loose',
  upc: '123456789',
  quantityOnHand: 100,
  price: 12.99,
  description: 'A test apparel',
  imageUrl: 'https://example.com/apparel.jpg',
  ...overrides,
});

export const createMockCustomer = (overrides = {}) => ({
  id: 1,
  version: 1,
  createdDate: '2023-01-01T00:00:00Z',
  updateDate: '2023-01-01T00:00:00Z',
  customerName: 'Test Customer',
  email: 'test@example.com',
  phone: '555-123-4567',
  address: '123 Test St',
  city: 'Test City',
  state: 'TS',
  zipCode: '12345',
  ...overrides,
});

export const createMockApparelOrder = (overrides = {}) => ({
  id: 1,
  version: 1,
  createdDate: '2023-01-01T00:00:00Z',
  updateDate: '2023-01-01T00:00:00Z',
  customerRef: 'CUST-123',
  paymentAmount: 25.98,
  status: 'NEW',
  apparelOrderLines: [
    {
      id: 1,
      version: 1,
      createdDate: '2023-01-01T00:00:00Z',
      updateDate: '2023-01-01T00:00:00Z',
      apparelId: 1,
      apparelName: 'Test Apparel',
      apparelStyle: 'Loose',
      upc: '123456789',
      orderQuantity: 2,
      quantityAllocated: 0,
      status: 'NEW',
    },
  ],
  shipments: [],
  ...overrides,
});

export function createMockPage<T>(content: T[], overrides = {}) {
  return {
    content,
    pageable: {
      sort: {
        sorted: false,
        unsorted: true,
        empty: true,
      },
      offset: 0,
      pageNumber: 0,
      pageSize: 20,
      paged: true,
      unpaged: false,
    },
    totalPages: 1,
    totalElements: content.length,
    last: true,
    size: 20,
    number: 0,
    sort: {
      sorted: false,
      unsorted: true,
      empty: true,
    },
    numberOfElements: content.length,
    first: true,
    empty: content.length === 0,
    ...overrides,
  };
}

// Form testing utilities
export const fillFormField = async (
  getByLabelText: (text: string) => HTMLElement,
  userEvent: {
    clear: (element: HTMLElement) => Promise<void>;
    type: (element: HTMLElement, text: string) => Promise<void>;
  },
  label: string,
  value: string
) => {
  const field = getByLabelText(label);
  await userEvent.clear(field);
  await userEvent.type(field, value);
};

export const selectOption = async (
  getByLabelText: (text: string) => HTMLElement,
  userEvent: { click: (element: HTMLElement) => Promise<void> },
  label: string,
  option: string
) => {
  const select = getByLabelText(label);
  await userEvent.click(select);
  const optionElement = document.querySelector(`[value="${option}"]`);
  if (optionElement) {
    await userEvent.click(optionElement as HTMLElement);
  }
};

// API response mocking utilities
export function mockApiResponse<T>(data: T, delay = 0): Promise<T> {
  return new Promise<T>(resolve => {
    setTimeout(() => resolve(data), delay);
  });
}

export const mockApiError = (message = 'API Error', status = 500, delay = 0) => {
  return new Promise((_, reject) => {
    setTimeout(() => {
      const error = new Error(message) as Error & {
        response: { status: number; data: { message: string } };
      };
      error.response = { status, data: { message } };
      reject(error);
    }, delay);
  });
};

// Accessibility testing utilities
export const checkAccessibility = async (container: HTMLElement) => {
  // This would integrate with axe-core for accessibility testing
  // For now, we'll do basic checks
  const buttons = container.querySelectorAll('button');
  const inputs = container.querySelectorAll('input, textarea, select');
  const images = container.querySelectorAll('img');

  // Check that buttons have accessible names
  buttons.forEach(button => {
    const hasAccessibleName =
      button.textContent?.trim() ||
      button.getAttribute('aria-label') ||
      button.getAttribute('aria-labelledby') ||
      button.getAttribute('title');

    if (!hasAccessibleName) {
      console.warn('Button without accessible name found:', button);
    }
  });

  // Check that form inputs have labels
  inputs.forEach(input => {
    const hasLabel =
      input.getAttribute('aria-label') ||
      input.getAttribute('aria-labelledby') ||
      document.querySelector(`label[for="${input.id}"]`);

    if (!hasLabel) {
      console.warn('Input without label found:', input);
    }
  });

  // Check that images have alt text
  images.forEach(img => {
    if (!img.getAttribute('alt')) {
      console.warn('Image without alt text found:', img);
    }
  });
};
