/**
 * Apparel Service
 *
 * Service for managing apparel-related operations
 */

import type { AxiosRequestConfig } from 'axios';
import apiService from './api';
import type { ApparelDto, ApparelPatchDto, PageOfApparelDto } from '../types/apparel';
import type { PaginationParams, SortParams, FilterParam } from '@utils/apiUtils';

// API endpoints
const BEER_API_URL = '/api/v1/apparels';
const BEER_BY_ID_URL = '/api/v1/apparels/{id}';

/**
 * Apparel Service class
 * Provides methods for interacting with the Apparel API
 */
class ApparelService {
  /**
   * Get a paginated list of apparels with optional filtering
   *
   * @param pagination - Pagination parameters
   * @param sort - Sorting parameters
   * @param apparelName - Optional apparel name filter
   * @param apparelStyle - Optional apparel style filter
   * @param config - Additional Axios request configuration
   * @returns Promise with the paginated apparel list
   */
  public async getApparels(
    pagination?: PaginationParams,
    sort?: SortParams,
    apparelName?: string,
    apparelStyle?: string,
    config?: AxiosRequestConfig
  ): Promise<PageOfApparelDto> {
    const filters: FilterParam[] = [];

    if (apparelName) {
      filters.push({ field: 'apparelName', value: apparelName });
    }

    if (apparelStyle) {
      filters.push({ field: 'apparelStyle', value: apparelStyle });
    }

    return apiService.getPaginatedWithNotification<PageOfApparelDto>(
      BEER_API_URL,
      pagination,
      sort,
      filters,
      config
    );
  }

  /**
   * Get a apparel by ID
   *
   * @param id - Apparel ID
   * @param config - Additional Axios request configuration
   * @returns Promise with the apparel
   */
  public async getApparelById(id: number, config?: AxiosRequestConfig): Promise<ApparelDto> {
    return apiService.getByIdWithNotification<ApparelDto>(BEER_BY_ID_URL, id, config);
  }

  /**
   * Create a new apparel
   *
   * @param apparel - Apparel data
   * @param config - Additional Axios request configuration
   * @returns Promise with the created apparel
   */
  public async createApparel(apparel: ApparelDto, config?: AxiosRequestConfig): Promise<ApparelDto> {
    return apiService.createWithNotification<ApparelDto>(BEER_API_URL, apparel, config);
  }

  /**
   * Update a apparel
   *
   * @param id - Apparel ID
   * @param apparel - Updated apparel data
   * @param config - Additional Axios request configuration
   * @returns Promise with the updated apparel
   */
  public async updateApparel(
    id: number,
    apparel: ApparelDto,
    config?: AxiosRequestConfig
  ): Promise<ApparelDto> {
    return apiService.updateWithNotification<ApparelDto>(BEER_BY_ID_URL, id, apparel, config);
  }

  /**
   * Partially update a apparel
   *
   * @param id - Apparel ID
   * @param apparelPatch - Partial apparel data
   * @param config - Additional Axios request configuration
   * @returns Promise with the updated apparel
   */
  public async patchApparel(
    id: number,
    apparelPatch: ApparelPatchDto,
    config?: AxiosRequestConfig
  ): Promise<ApparelDto> {
    return apiService.partialUpdateWithNotification<ApparelDto>(BEER_BY_ID_URL, id, apparelPatch, config);
  }

  /**
   * Delete a apparel
   *
   * @param id - Apparel ID
   * @param config - Additional Axios request configuration
   * @returns Promise with the response
   */
  public async deleteApparel(id: number, config?: AxiosRequestConfig): Promise<void> {
    return apiService.deleteResourceWithNotification<void>(BEER_BY_ID_URL, id, config);
  }
}

// Create a singleton instance
const apparelService = new ApparelService();

export default apparelService;
