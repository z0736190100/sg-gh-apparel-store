/**
 * Apparel Order-related type definitions
 */

/**
 * Apparel Order Line DTO interface
 * Represents a line item in a apparel order
 */
export interface ApparelOrderLineDto {
  id?: number;
  version?: number;
  createdDate?: string;
  updateDate?: string;
  apparelId: number;
  apparelName: string;
  apparelStyle: string;
  upc: string;
  orderQuantity: number;
  quantityAllocated?: number;
  status?: string;
}

/**
 * Apparel Order Shipment DTO interface
 * Represents a shipment for a apparel order
 */
export interface ApparelOrderShipmentDto {
  id?: number;
  version?: number;
  createdDate?: string;
  updateDate?: string;
  shipmentDate: string;
  carrier?: string;
  trackingNumber?: string;
}

/**
 * Apparel Order DTO interface
 * Represents a apparel order entity in the system
 */
export interface ApparelOrderDto {
  id?: number;
  version?: number;
  createdDate?: string;
  updateDate?: string;
  customerRef?: string;
  paymentAmount: number;
  status?: string;
  apparelOrderLines: ApparelOrderLineDto[];
  shipments?: ApparelOrderShipmentDto[];
}

/**
 * Apparel Order Patch DTO interface
 * Used for partial updates to a apparel order entity
 */
export interface ApparelOrderPatchDto {
  id?: number;
  version?: number;
  createdDate?: string;
  updateDate?: string;
  customerRef?: string;
  paymentAmount?: number;
  status?: string;
  apparelOrderLines?: ApparelOrderLineDto[];
  shipments?: ApparelOrderShipmentDto[];
}

/**
 * Page of Apparel Order DTO interface
 * Represents a paginated list of apparel orders
 */
export type PageOfApparelOrderDto = Page<ApparelOrderDto>;

/**
 * Import the Page interface from apparel.ts
 * This is a workaround until we move the Page interface to a common location
 */
import type { Page } from './apparel';
