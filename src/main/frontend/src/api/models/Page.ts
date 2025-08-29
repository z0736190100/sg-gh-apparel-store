/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * A page of results
 */
export type Page = {
    /**
     * The page content
     */
    content?: Array<Record<string, any>>;
    /**
     * The pagination information
     */
    pageable?: {
        sort?: {
            sorted?: boolean;
            unsorted?: boolean;
            empty?: boolean;
        };
        offset?: number;
        pageNumber?: number;
        pageSize?: number;
        paged?: boolean;
        unpaged?: boolean;
    };
    /**
     * The total number of pages
     */
    totalPages?: number;
    /**
     * The total number of elements
     */
    totalElements?: number;
    /**
     * Whether this is the last page
     */
    last?: boolean;
    /**
     * The size of the page
     */
    size?: number;
    /**
     * The page number (0-based)
     */
    number?: number;
    sort?: {
        sorted?: boolean;
        unsorted?: boolean;
        empty?: boolean;
    };
    /**
     * The number of elements in this page
     */
    numberOfElements?: number;
    /**
     * Whether this is the first page
     */
    first?: boolean;
    /**
     * Whether the page is empty
     */
    empty?: boolean;
};

