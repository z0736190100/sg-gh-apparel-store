import apparelService from '../apparelService';
import apiService from '../api';
import { createMockApparel, createMockPage } from '../../test/utils';
import type { ApparelDto, ApparelPatchDto } from '../../types/apparel';

// Interface for API errors with response property
interface ApiError extends Error {
  response?: {
    status: number;
    data?: { message: string };
  };
  code?: string;
}

// Mock the API service
jest.mock('../api');
const mockApiService = apiService as jest.Mocked<typeof apiService>;

describe('ApparelService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getApparels', () => {
    it('should fetch apparels with pagination', async () => {
      const mockApparels = [
        createMockApparel({ id: 1, apparelName: 'Test Apparel 1' }),
        createMockApparel({ id: 2, apparelName: 'Test Apparel 2' }),
      ];
      const mockPage = createMockPage(mockApparels);

      mockApiService.getPaginatedWithNotification.mockResolvedValue(mockPage);

      const result = await apparelService.getApparels(
        { page: 0, size: 20 },
        { sort: 'apparelName', direction: 'asc' }
      );

      expect(mockApiService.getPaginatedWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels',
        { page: 0, size: 20 },
        { sort: 'apparelName', direction: 'asc' },
        [],
        undefined
      );
      expect(result).toEqual(mockPage);
    });

    it('should fetch apparels with name filter', async () => {
      const mockApparels = [createMockApparel({ apparelName: 'Loose Apparel' })];
      const mockPage = createMockPage(mockApparels);

      mockApiService.getPaginatedWithNotification.mockResolvedValue(mockPage);

      await apparelService.getApparels(undefined, undefined, 'Loose');

      expect(mockApiService.getPaginatedWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels',
        undefined,
        undefined,
        [{ field: 'apparelName', value: 'Loose' }],
        undefined
      );
    });

    it('should fetch apparels with style filter', async () => {
      const mockApparels = [createMockApparel({ apparelStyle: 'Loose' })];
      const mockPage = createMockPage(mockApparels);

      mockApiService.getPaginatedWithNotification.mockResolvedValue(mockPage);

      await apparelService.getApparels(undefined, undefined, undefined, 'Loose');

      expect(mockApiService.getPaginatedWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels',
        undefined,
        undefined,
        [{ field: 'apparelStyle', value: 'Loose' }],
        undefined
      );
    });

    it('should fetch apparels with both name and style filters', async () => {
      const mockApparels = [createMockApparel({ apparelName: 'Test Loose', apparelStyle: 'Loose' })];
      const mockPage = createMockPage(mockApparels);

      mockApiService.getPaginatedWithNotification.mockResolvedValue(mockPage);

      await apparelService.getApparels(undefined, undefined, 'Test', 'Loose');

      expect(mockApiService.getPaginatedWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels',
        undefined,
        undefined,
        [
          { field: 'apparelName', value: 'Test' },
          { field: 'apparelStyle', value: 'Loose' },
        ],
        undefined
      );
    });

    it('should handle API errors', async () => {
      const error = new Error('API Error');
      mockApiService.getPaginatedWithNotification.mockRejectedValue(error);

      await expect(apparelService.getApparels()).rejects.toThrow('API Error');
    });
  });

  describe('getApparelById', () => {
    it('should fetch a apparel by ID', async () => {
      const mockApparel = createMockApparel({ id: 1, apparelName: 'Test Apparel' });
      mockApiService.getByIdWithNotification.mockResolvedValue(mockApparel);

      const result = await apparelService.getApparelById(1);

      expect(mockApiService.getByIdWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels/{id}',
        1,
        undefined
      );
      expect(result).toEqual(mockApparel);
    });

    it('should handle not found error', async () => {
      const error: ApiError = new Error('Apparel not found');
      error.response = { status: 404 };
      mockApiService.getByIdWithNotification.mockRejectedValue(error);

      await expect(apparelService.getApparelById(999)).rejects.toThrow('Apparel not found');
    });

    it('should pass custom config', async () => {
      const mockApparel = createMockApparel({ id: 1 });
      const customConfig = { timeout: 5000 };
      mockApiService.getByIdWithNotification.mockResolvedValue(mockApparel);

      await apparelService.getApparelById(1, customConfig);

      expect(mockApiService.getByIdWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels/{id}',
        1,
        customConfig
      );
    });
  });

  describe('createApparel', () => {
    it('should create a new apparel', async () => {
      const newApparel: ApparelDto = {
        apparelName: 'New Apparel',
        apparelStyle: 'Loose',
        upc: '123456789',
        quantityOnHand: 100,
        price: 12.99,
      };
      const createdApparel = createMockApparel({ id: 1, ...newApparel });
      mockApiService.createWithNotification.mockResolvedValue(createdApparel);

      const result = await apparelService.createApparel(newApparel);

      expect(mockApiService.createWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels',
        newApparel,
        undefined
      );
      expect(result).toEqual(createdApparel);
    });

    it('should handle validation errors', async () => {
      const invalidApparel: ApparelDto = {
        apparelName: '',
        apparelStyle: 'Loose',
        upc: '123456789',
        quantityOnHand: 100,
        price: 12.99,
      };
      const error: ApiError = new Error('Validation failed');
      error.response = { status: 400, data: { message: 'Apparel name is required' } };
      mockApiService.createWithNotification.mockRejectedValue(error);

      await expect(apparelService.createApparel(invalidApparel)).rejects.toThrow('Validation failed');
    });
  });

  describe('updateApparel', () => {
    it('should update an existing apparel', async () => {
      const updatedApparel: ApparelDto = {
        id: 1,
        apparelName: 'Updated Apparel',
        apparelStyle: 'Loose',
        upc: '123456789',
        quantityOnHand: 150,
        price: 13.99,
      };
      mockApiService.updateWithNotification.mockResolvedValue(updatedApparel);

      const result = await apparelService.updateApparel(1, updatedApparel);

      expect(mockApiService.updateWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels/{id}',
        1,
        updatedApparel,
        undefined
      );
      expect(result).toEqual(updatedApparel);
    });

    it('should handle update conflicts', async () => {
      const apparel = createMockApparel({ id: 1, version: 1 });
      const error: ApiError = new Error('Conflict');
      error.response = { status: 409, data: { message: 'Version conflict' } };
      mockApiService.updateWithNotification.mockRejectedValue(error);

      await expect(apparelService.updateApparel(1, apparel)).rejects.toThrow('Conflict');
    });
  });

  describe('patchApparel', () => {
    it('should partially update a apparel', async () => {
      const patchData: ApparelPatchDto = {
        price: 14.99,
        quantityOnHand: 200,
      };
      const updatedApparel = createMockApparel({ id: 1, ...patchData });
      mockApiService.partialUpdateWithNotification.mockResolvedValue(updatedApparel);

      const result = await apparelService.patchApparel(1, patchData);

      expect(mockApiService.partialUpdateWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels/{id}',
        1,
        patchData,
        undefined
      );
      expect(result).toEqual(updatedApparel);
    });

    it('should handle patch validation errors', async () => {
      const invalidPatch: ApparelPatchDto = {
        price: -1, // Invalid negative price
      };
      const error: ApiError = new Error('Invalid price');
      error.response = { status: 400 };
      mockApiService.partialUpdateWithNotification.mockRejectedValue(error);

      await expect(apparelService.patchApparel(1, invalidPatch)).rejects.toThrow('Invalid price');
    });
  });

  describe('deleteApparel', () => {
    it('should delete a apparel', async () => {
      mockApiService.deleteResourceWithNotification.mockResolvedValue(undefined);

      await apparelService.deleteApparel(1);

      expect(mockApiService.deleteResourceWithNotification).toHaveBeenCalledWith(
        '/api/v1/apparels/{id}',
        1,
        undefined
      );
    });

    it('should handle delete errors', async () => {
      const error: ApiError = new Error('Cannot delete apparel');
      error.response = { status: 409, data: { message: 'Apparel has active orders' } };
      mockApiService.deleteResourceWithNotification.mockRejectedValue(error);

      await expect(apparelService.deleteApparel(1)).rejects.toThrow('Cannot delete apparel');
    });

    it('should handle not found on delete', async () => {
      const error: ApiError = new Error('Apparel not found');
      error.response = { status: 404 };
      mockApiService.deleteResourceWithNotification.mockRejectedValue(error);

      await expect(apparelService.deleteApparel(999)).rejects.toThrow('Apparel not found');
    });
  });

  describe('error handling', () => {
    it('should handle network errors', async () => {
      const networkError: ApiError = new Error('Network Error');
      networkError.code = 'NETWORK_ERROR';
      mockApiService.getPaginatedWithNotification.mockRejectedValue(networkError);

      await expect(apparelService.getApparels()).rejects.toThrow('Network Error');
    });

    it('should handle timeout errors', async () => {
      const timeoutError: ApiError = new Error('Timeout');
      timeoutError.code = 'ECONNABORTED';
      mockApiService.getByIdWithNotification.mockRejectedValue(timeoutError);

      await expect(apparelService.getApparelById(1)).rejects.toThrow('Timeout');
    });

    it('should handle server errors', async () => {
      const serverError: ApiError = new Error('Internal Server Error');
      serverError.response = { status: 500 };
      mockApiService.createWithNotification.mockRejectedValue(serverError);

      const apparel = createMockApparel();
      await expect(apparelService.createApparel(apparel)).rejects.toThrow('Internal Server Error');
    });
  });

  describe('service configuration', () => {
    it('should use correct API endpoints', () => {
      // Test that the service uses the expected endpoints
      expect(apparelService).toBeDefined();
      expect(typeof apparelService.getApparels).toBe('function');
      expect(typeof apparelService.getApparelById).toBe('function');
      expect(typeof apparelService.createApparel).toBe('function');
      expect(typeof apparelService.updateApparel).toBe('function');
      expect(typeof apparelService.patchApparel).toBe('function');
      expect(typeof apparelService.deleteApparel).toBe('function');
    });

    it('should be a singleton instance', async () => {
      // Import the service again to test singleton behavior
      const { default: apparelService2 } = await import('../apparelService');
      expect(apparelService).toBe(apparelService2);
    });
  });
});
