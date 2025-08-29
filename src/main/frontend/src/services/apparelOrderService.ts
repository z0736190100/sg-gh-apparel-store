/**
 * Apparel Order Service
 *
 * Service for managing apparel order-related operations
 */

import type { AxiosRequestConfig } from 'axios';
import apiService from './api';
import type {
  ApparelOrderDto,
  ApparelOrderPatchDto,
  ApparelOrderShipmentDto,
  PageOfApparelOrderDto,
} from '../types/apparelOrder';
import type { PaginationParams, SortParams, FilterParam } from '@utils/apiUtils';

// API endpoints
const BEER_ORDER_API_URL = '/api/v1/apparel-orders';
const BEER_ORDER_BY_ID_URL = '/api/v1/apparel-orders/{id}';
const BEER_ORDER_SHIPMENTS_URL = '/api/v1/apparel-orders/{apparelOrderId}/shipments';
const BEER_ORDER_SHIPMENT_BY_ID_URL = '/api/v1/apparel-orders/{apparelOrderId}/shipments/{shipmentId}';

/**
 * Apparel Order Service class
 * Provides methods for interacting with the Apparel Order API
 */
class ApparelOrderService {
  /**
   * Get all apparel orders
   *
   * @param config - Additional Axios request configuration
   * @returns Promise with the list of apparel orders
   */
  public async getApparelOrders(config?: AxiosRequestConfig): Promise<ApparelOrderDto[]> {
    return apiService.getWithNotification<ApparelOrderDto[]>(BEER_ORDER_API_URL, config);
  }

  /**
   * Get a paginated list of apparel orders
   * Note: The API doesn't natively support pagination for apparel orders,
   * so we implement client-side pagination
   *
   * @param pagination - Pagination parameters
   * @param sort - Sorting parameters
   * @param filters - Filter parameters
   * @param config - Additional Axios request configuration
   * @returns Promise with the paginated apparel order list
   */
  public async getApparelOrdersPaginated(
    pagination?: PaginationParams,
    sort?: SortParams,
    filters?: FilterParam[],
    config?: AxiosRequestConfig
  ): Promise<PageOfApparelOrderDto> {
    // Get all apparel orders
    const apparelOrders = await this.getApparelOrders(config);

    // Apply filters if provided
    let filteredApparelOrders = apparelOrders;
    if (filters && filters.length > 0) {
      filteredApparelOrders = this.applyFilters(apparelOrders, filters);
    }

    // Apply sorting if provided
    if (sort && sort.sort) {
      this.applySorting(filteredApparelOrders, sort);
    }

    // Apply pagination if provided
    const page = pagination?.page || 0;
    const size = pagination?.size || 20;
    const start = page * size;
    const end = start + size;
    const paginatedApparelOrders = filteredApparelOrders.slice(start, end);

    // Create a paginated response
    return {
      content: paginatedApparelOrders,
      pageable: {
        sort: {
          sorted: !!sort?.sort,
          unsorted: !sort?.sort,
          empty: !sort?.sort,
        },
        offset: start,
        pageNumber: page,
        pageSize: size,
        paged: true,
        unpaged: false,
      },
      totalPages: Math.ceil(filteredApparelOrders.length / size),
      totalElements: filteredApparelOrders.length,
      last: start + size >= filteredApparelOrders.length,
      size: size,
      number: page,
      sort: {
        sorted: !!sort?.sort,
        unsorted: !sort?.sort,
        empty: !sort?.sort,
      },
      numberOfElements: paginatedApparelOrders.length,
      first: page === 0,
      empty: paginatedApparelOrders.length === 0,
    };
  }

  /**
   * Apply filters to apparel orders
   *
   * @param apparelOrders - List of apparel orders
   * @param filters - Filter parameters
   * @returns Filtered list of apparel orders
   */
  private applyFilters(apparelOrders: ApparelOrderDto[], filters: FilterParam[]): ApparelOrderDto[] {
    return apparelOrders.filter(apparelOrder => {
      return filters.every(filter => {
        const field = filter.field;
        const value = filter.value;

        if (field === 'customerRef' && typeof value === 'string') {
          return apparelOrder.customerRef?.toLowerCase().includes(value.toLowerCase());
        }

        if (field === 'status' && typeof value === 'string') {
          return apparelOrder.status?.toLowerCase() === value.toLowerCase();
        }

        return true;
      });
    });
  }

  /**
   * Apply sorting to apparel orders
   *
   * @param apparelOrders - List of apparel orders
   * @param sort - Sorting parameters
   */
  private applySorting(apparelOrders: ApparelOrderDto[], sort: SortParams): void {
    const field = sort.sort;
    const direction = sort.direction || 'asc';

    if (!field) return;

    apparelOrders.sort((a, b) => {
      let valueA: string | number;
      let valueB: string | number;

      switch (field) {
        case 'id':
          valueA = a.id || 0;
          valueB = b.id || 0;
          break;
        case 'customerRef':
          valueA = a.customerRef || '';
          valueB = b.customerRef || '';
          break;
        case 'status':
          valueA = a.status || '';
          valueB = b.status || '';
          break;
        case 'createdDate':
          valueA = a.createdDate ? new Date(a.createdDate).getTime() : 0;
          valueB = b.createdDate ? new Date(b.createdDate).getTime() : 0;
          break;
        case 'paymentAmount':
          valueA = a.paymentAmount || 0;
          valueB = b.paymentAmount || 0;
          break;
        default:
          return 0;
      }

      if (typeof valueA === 'string' && typeof valueB === 'string') {
        return direction === 'asc' ? valueA.localeCompare(valueB) : valueB.localeCompare(valueA);
      }

      if (typeof valueA === 'number' && typeof valueB === 'number') {
        return direction === 'asc' ? valueA - valueB : valueB - valueA;
      }

      return 0;
    });
  }

  /**
   * Get a apparel order by ID
   *
   * @param id - Apparel order ID
   * @param config - Additional Axios request configuration
   * @returns Promise with the apparel order
   */
  public async getApparelOrderById(id: number, config?: AxiosRequestConfig): Promise<ApparelOrderDto> {
    return apiService.getByIdWithNotification<ApparelOrderDto>(BEER_ORDER_BY_ID_URL, id, config);
  }

  /**
   * Create a new apparel order
   *
   * @param apparelOrder - Apparel order data
   * @param config - Additional Axios request configuration
   * @returns Promise with the created apparel order
   */
  public async createApparelOrder(
    apparelOrder: ApparelOrderDto,
    config?: AxiosRequestConfig
  ): Promise<ApparelOrderDto> {
    return apiService.createWithNotification<ApparelOrderDto>(BEER_ORDER_API_URL, apparelOrder, config);
  }

  /**
   * Update a apparel order
   *
   * @param id - Apparel order ID
   * @param apparelOrder - Updated apparel order data
   * @param config - Additional Axios request configuration
   * @returns Promise with the updated apparel order
   */
  public async updateApparelOrder(
    id: number,
    apparelOrder: ApparelOrderDto,
    config?: AxiosRequestConfig
  ): Promise<ApparelOrderDto> {
    return apiService.updateWithNotification<ApparelOrderDto>(
      BEER_ORDER_BY_ID_URL,
      id,
      apparelOrder,
      config
    );
  }

  /**
   * Delete a apparel order
   *
   * @param id - Apparel order ID
   * @param config - Additional Axios request configuration
   * @returns Promise with the response
   */
  public async deleteApparelOrder(id: number, config?: AxiosRequestConfig): Promise<void> {
    return apiService.deleteResourceWithNotification<void>(BEER_ORDER_BY_ID_URL, id, config);
  }

  /**
   * Update apparel order status
   *
   * @param id - Apparel order ID
   * @param status - New status
   * @param config - Additional Axios request configuration
   * @returns Promise with the updated apparel order
   */
  public async updateApparelOrderStatus(
    id: number,
    status: string,
    config?: AxiosRequestConfig
  ): Promise<ApparelOrderDto> {
    const apparelOrderPatch: ApparelOrderPatchDto = {
      status,
    };
    return apiService.partialUpdateWithNotification<ApparelOrderDto>(
      BEER_ORDER_BY_ID_URL,
      id,
      apparelOrderPatch,
      config
    );
  }

  /**
   * Get all shipments for a apparel order
   *
   * @param apparelOrderId - Apparel order ID
   * @param config - Additional Axios request configuration
   * @returns Promise with the list of shipments
   */
  public async getApparelOrderShipments(
    apparelOrderId: number,
    config?: AxiosRequestConfig
  ): Promise<ApparelOrderShipmentDto[]> {
    const url = BEER_ORDER_SHIPMENTS_URL.replace('{apparelOrderId}', apparelOrderId.toString());
    return apiService.getWithNotification<ApparelOrderShipmentDto[]>(url, config);
  }

  /**
   * Get a specific shipment for a apparel order
   *
   * @param apparelOrderId - Apparel order ID
   * @param shipmentId - Shipment ID
   * @param config - Additional Axios request configuration
   * @returns Promise with the shipment
   */
  public async getApparelOrderShipmentById(
    apparelOrderId: number,
    shipmentId: number,
    config?: AxiosRequestConfig
  ): Promise<ApparelOrderShipmentDto> {
    const url = BEER_ORDER_SHIPMENT_BY_ID_URL.replace(
      '{apparelOrderId}',
      apparelOrderId.toString()
    ).replace('{shipmentId}', shipmentId.toString());
    return apiService.getWithNotification<ApparelOrderShipmentDto>(url, config);
  }

  /**
   * Create a new shipment for a apparel order
   *
   * @param apparelOrderId - Apparel order ID
   * @param shipment - Shipment data
   * @param config - Additional Axios request configuration
   * @returns Promise with the created shipment
   */
  public async createApparelOrderShipment(
    apparelOrderId: number,
    shipment: ApparelOrderShipmentDto,
    config?: AxiosRequestConfig
  ): Promise<ApparelOrderShipmentDto> {
    const url = BEER_ORDER_SHIPMENTS_URL.replace('{apparelOrderId}', apparelOrderId.toString());
    return apiService.createWithNotification<ApparelOrderShipmentDto>(url, shipment, config);
  }

  /**
   * Update a shipment for a apparel order
   *
   * @param apparelOrderId - Apparel order ID
   * @param shipmentId - Shipment ID
   * @param shipment - Updated shipment data
   * @param config - Additional Axios request configuration
   * @returns Promise with the updated shipment
   */
  public async updateApparelOrderShipment(
    apparelOrderId: number,
    shipmentId: number,
    shipment: ApparelOrderShipmentDto,
    config?: AxiosRequestConfig
  ): Promise<ApparelOrderShipmentDto> {
    const url = BEER_ORDER_SHIPMENT_BY_ID_URL.replace(
      '{apparelOrderId}',
      apparelOrderId.toString()
    ).replace('{shipmentId}', shipmentId.toString());
    return apiService.updateWithNotification<ApparelOrderShipmentDto>(
      url,
      shipmentId,
      shipment,
      config
    );
  }

  /**
   * Delete a shipment for a apparel order
   *
   * @param apparelOrderId - Apparel order ID
   * @param shipmentId - Shipment ID
   * @param config - Additional Axios request configuration
   * @returns Promise with the response
   */
  public async deleteApparelOrderShipment(
    apparelOrderId: number,
    shipmentId: number,
    config?: AxiosRequestConfig
  ): Promise<void> {
    const url = BEER_ORDER_SHIPMENT_BY_ID_URL.replace(
      '{apparelOrderId}',
      apparelOrderId.toString()
    ).replace('{shipmentId}', shipmentId.toString());
    return apiService.deleteWithNotification<void>(url, config);
  }
}

// Create a singleton instance
const apparelOrderService = new ApparelOrderService();

export default apparelOrderService;
