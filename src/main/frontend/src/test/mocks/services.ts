import { createMockApparel, createMockCustomer, createMockApparelOrder, createMockPage } from '../utils';
import type { ApparelDto } from '../../types/apparel';
import type { CustomerDto } from '../../types/customer';
import type { ApparelOrderDto, ApparelOrderShipmentDto } from '../../types/apparelOrder';

// Mock Apparel Service
export const mockApparelService = {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  getApparels: jest.fn().mockImplementation((_params = {}) => {
    const mockApparels = [
      createMockApparel({ id: 1, apparelName: 'Mango Bobs', apparelStyle: 'IPA' }),
      createMockApparel({ id: 2, apparelName: 'Galaxy Cat', apparelStyle: 'PALE_ALE' }),
      createMockApparel({ id: 3, apparelName: 'Pinball Porter', apparelStyle: 'PORTER' }),
    ];
    return Promise.resolve(createMockPage(mockApparels));
  }),

  getApparelById: jest.fn().mockImplementation((id: number) => {
    const mockApparel = createMockApparel({ id, apparelName: `Test Apparel ${id}` });
    return Promise.resolve(mockApparel);
  }),

  createApparel: jest
    .fn()
    .mockImplementation(
      (apparelData: Omit<ApparelDto, 'id' | 'version' | 'createdDate' | 'updateDate'>) => {
        const mockApparel = createMockApparel({ id: 999, ...apparelData });
        return Promise.resolve(mockApparel);
      }
    ),

  updateApparel: jest.fn().mockImplementation((id: number, apparelData: Partial<ApparelDto>) => {
    const mockApparel = createMockApparel({ id, ...apparelData });
    return Promise.resolve(mockApparel);
  }),

  patchApparel: jest.fn().mockImplementation((id: number, patchData: Partial<ApparelDto>) => {
    const mockApparel = createMockApparel({ id, ...patchData });
    return Promise.resolve(mockApparel);
  }),

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  deleteApparel: jest.fn().mockImplementation((_id: number) => {
    return Promise.resolve();
  }),
};

// Mock Customer Service
export const mockCustomerService = {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  getCustomers: jest.fn().mockImplementation((_params = {}) => {
    const mockCustomers = [
      createMockCustomer({ id: 1, customerName: 'John Doe', email: 'john@example.com' }),
      createMockCustomer({ id: 2, customerName: 'Jane Smith', email: 'jane@example.com' }),
      createMockCustomer({ id: 3, customerName: 'Bob Johnson', email: 'bob@example.com' }),
    ];
    return Promise.resolve(createMockPage(mockCustomers));
  }),

  getCustomerById: jest.fn().mockImplementation((id: number) => {
    const mockCustomer = createMockCustomer({ id, customerName: `Test Customer ${id}` });
    return Promise.resolve(mockCustomer);
  }),

  createCustomer: jest
    .fn()
    .mockImplementation(
      (customerData: Omit<CustomerDto, 'id' | 'version' | 'createdDate' | 'updateDate'>) => {
        const mockCustomer = createMockCustomer({ id: 999, ...customerData });
        return Promise.resolve(mockCustomer);
      }
    ),

  updateCustomer: jest.fn().mockImplementation((id: number, customerData: Partial<CustomerDto>) => {
    const mockCustomer = createMockCustomer({ id, ...customerData });
    return Promise.resolve(mockCustomer);
  }),

  patchCustomer: jest.fn().mockImplementation((id: number, patchData: Partial<CustomerDto>) => {
    const mockCustomer = createMockCustomer({ id, ...patchData });
    return Promise.resolve(mockCustomer);
  }),

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  deleteCustomer: jest.fn().mockImplementation((_id: number) => {
    return Promise.resolve();
  }),
};

// Mock Apparel Order Service
export const mockApparelOrderService = {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  getApparelOrders: jest.fn().mockImplementation((_params = {}) => {
    const mockOrders = [
      createMockApparelOrder({ id: 1, customerRef: 'CUST-001', status: 'NEW' }),
      createMockApparelOrder({ id: 2, customerRef: 'CUST-002', status: 'PROCESSING' }),
      createMockApparelOrder({ id: 3, customerRef: 'CUST-003', status: 'COMPLETED' }),
    ];
    return Promise.resolve(createMockPage(mockOrders));
  }),

  getApparelOrderById: jest.fn().mockImplementation((id: number) => {
    const mockOrder = createMockApparelOrder({
      id,
      customerRef: `CUST-${id.toString().padStart(3, '0')}`,
    });
    return Promise.resolve(mockOrder);
  }),

  createApparelOrder: jest
    .fn()
    .mockImplementation(
      (orderData: Omit<ApparelOrderDto, 'id' | 'version' | 'createdDate' | 'updateDate'>) => {
        const mockOrder = createMockApparelOrder({ id: 999, ...orderData });
        return Promise.resolve(mockOrder);
      }
    ),

  updateApparelOrder: jest.fn().mockImplementation((id: number, orderData: Partial<ApparelOrderDto>) => {
    const mockOrder = createMockApparelOrder({ id, ...orderData });
    return Promise.resolve(mockOrder);
  }),

  patchApparelOrder: jest.fn().mockImplementation((id: number, patchData: Partial<ApparelOrderDto>) => {
    const mockOrder = createMockApparelOrder({ id, ...patchData });
    return Promise.resolve(mockOrder);
  }),

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  deleteApparelOrder: jest.fn().mockImplementation((_id: number) => {
    return Promise.resolve();
  }),

  createApparelOrderShipment: jest
    .fn()
    .mockImplementation(
      (
        _orderId: number,
        shipmentData: Omit<ApparelOrderShipmentDto, 'id' | 'version' | 'createdDate' | 'updateDate'>
      ) => {
        const mockShipment: ApparelOrderShipmentDto = {
          id: 999,
          version: 1,
          createdDate: new Date().toISOString(),
          updateDate: new Date().toISOString(),
          ...shipmentData,
        };
        return Promise.resolve(mockShipment);
      }
    ),

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  getApparelOrderShipments: jest.fn().mockImplementation((_orderId: number) => {
    const mockShipments: ApparelOrderShipmentDto[] = [
      {
        id: 1,
        version: 1,
        createdDate: '2023-01-01T00:00:00Z',
        updateDate: '2023-01-01T00:00:00Z',
        shipmentDate: '2023-01-02T10:00:00Z',
        carrier: 'FedEx',
        trackingNumber: '123456789',
      },
    ];
    return Promise.resolve(mockShipments);
  }),
};

// Mock API Error Responses
export const mockApiErrors = {
  notFound: {
    response: {
      status: 404,
      data: { message: 'Resource not found' },
    },
  },
  badRequest: {
    response: {
      status: 400,
      data: { message: 'Bad request' },
    },
  },
  unauthorized: {
    response: {
      status: 401,
      data: { message: 'Unauthorized' },
    },
  },
  forbidden: {
    response: {
      status: 403,
      data: { message: 'Forbidden' },
    },
  },
  serverError: {
    response: {
      status: 500,
      data: { message: 'Internal server error' },
    },
  },
};

// Helper function to reset all mocks
export const resetAllMocks = () => {
  Object.values(mockApparelService).forEach(mock => {
    if (jest.isMockFunction(mock)) {
      mock.mockClear();
    }
  });

  Object.values(mockCustomerService).forEach(mock => {
    if (jest.isMockFunction(mock)) {
      mock.mockClear();
    }
  });

  Object.values(mockApparelOrderService).forEach(mock => {
    if (jest.isMockFunction(mock)) {
      mock.mockClear();
    }
  });
};

// Helper function to make a mock service method reject with an error
export const makeMockReject = <T = unknown>(
  mockFn: jest.MockedFunction<(...args: unknown[]) => Promise<T>>,
  error: unknown
) => {
  mockFn.mockRejectedValueOnce(error);
};

// Helper function to make a mock service method resolve with custom data
export const makeMockResolve = <T = unknown>(
  mockFn: jest.MockedFunction<(...args: unknown[]) => Promise<T>>,
  data: T
) => {
  mockFn.mockResolvedValueOnce(data);
};
