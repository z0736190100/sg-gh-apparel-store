/**
 * Apparel-related type definitions
 */

/**
 * Apparel DTO interface
 * Represents a apparel entity in the system
 */
export interface ApparelDto {
  id?: number;
  version?: number;
  createdDate?: string;
  updateDate?: string;
  apparelName: string;
  apparelStyle: string;
  upc: string;
  quantityOnHand: number;
  price: number;
  description?: string;
  imageUrl?: string;
}

/**
 * Apparel Patch DTO interface
 * Used for partial updates to a apparel entity
 */
export interface ApparelPatchDto {
  id?: number;
  version?: number;
  createdDate?: string;
  updateDate?: string;
  apparelName?: string;
  apparelStyle?: string;
  upc?: string;
  quantityOnHand?: number;
  price?: number;
  description?: string;
  imageUrl?: string;
}

/**
 * Page interface
 * Generic pagination structure
 */
export interface Page<T> {
  content: T[];
  pageable: {
    sort: {
      sorted: boolean;
      unsorted: boolean;
      empty: boolean;
    };
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

/**
 * Page of Apparel DTO interface
 * Represents a paginated list of apparels
 */
export type PageOfApparelDto = Page<ApparelDto>;
