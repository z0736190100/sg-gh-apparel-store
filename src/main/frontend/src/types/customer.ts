/**
 * Customer-related type definitions
 */

/**
 * Customer DTO interface
 * Represents a customer entity in the system
 */
export interface CustomerDto {
  id?: number;
  version?: number;
  createdDate?: string;
  updateDate?: string;
  name: string;
  email?: string;
  phoneNumber?: string;
  addressLine1: string;
  addressLine2?: string;
  city: string;
  state: string;
  postalCode: string;
  apparelOrders?: import('./apparelOrder').ApparelOrderDto[];
}

/**
 * Customer Patch DTO interface
 * Used for partial updates to a customer entity
 */
export interface CustomerPatchDto {
  id?: number;
  version?: number;
  createdDate?: string;
  updateDate?: string;
  name?: string;
  email?: string;
  phoneNumber?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  postalCode?: string;
}

/**
 * Page of Customer DTO interface
 * Represents a paginated list of customers
 */
export type PageOfCustomerDto = Page<CustomerDto>;

/**
 * Import the Page interface from apparel.ts
 * This is a workaround until we move the Page interface to a common location
 */
import type { Page } from './apparel';
