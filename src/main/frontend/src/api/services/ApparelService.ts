/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApparelDto } from '../models/ApparelDto';
import type { ApparelPatchDto } from '../models/ApparelPatchDto';
import type { PageOfApparelDto } from '../models/PageOfApparelDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ApparelService {
    /**
     * Get all apparels
     * Returns a paginated list of apparels in the system with optional filtering by apparel name and apparel style.
     * @param apparelName Filter apparels by name (optional)
     * @param apparelStyle Filter apparels by style (optional)
     * @param page Page number (0-based, defaults to 0)
     * @param size Page size (defaults to 20)
     * @returns PageOfApparelDto Successfully retrieved the list of apparels
     * @throws ApiError
     */
    public static getAllApparels(
        apparelName?: string,
        apparelStyle?: string,
        page?: number,
        size: number = 20,
    ): CancelablePromise<PageOfApparelDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/v1/apparels',
            query: {
                'apparelName': apparelName,
                'apparelStyle': apparelStyle,
                'page': page,
                'size': size,
            },
            errors: {
                400: `Bad request`,
                401: `Unauthorized`,
            },
        });
    }
    /**
     * Create a new apparel
     * Creates a new apparel in the system. The ID, version, createdDate, and updateDate fields will be ignored if provided.
     * @param requestBody Apparel object to be created
     * @returns ApparelDto Successfully created a new apparel
     * @throws ApiError
     */
    public static createApparel(
        requestBody: ApparelDto,
    ): CancelablePromise<ApparelDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/v1/apparels',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request - validation error`,
                401: `Unauthorized`,
            },
        });
    }
    /**
     * Get a apparel by ID
     * Returns a single apparel identified by its ID.
     * @param id ID of the apparel to operate on
     * @returns ApparelDto Successfully retrieved the apparel
     * @throws ApiError
     */
    public static getApparelById(
        id: number,
    ): CancelablePromise<ApparelDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/v1/apparels/{id}',
            path: {
                'id': id,
            },
            errors: {
                400: `Bad request`,
                401: `Unauthorized`,
                404: `Apparel not found`,
            },
        });
    }
    /**
     * Update a apparel
     * Updates an existing apparel identified by its ID. The ID in the path must match the ID in the request body.
     * @param id ID of the apparel to operate on
     * @param requestBody Updated apparel object
     * @returns ApparelDto Successfully updated the apparel
     * @throws ApiError
     */
    public static updateApparel(
        id: number,
        requestBody: ApparelDto,
    ): CancelablePromise<ApparelDto> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/v1/apparels/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request - validation error`,
                401: `Unauthorized`,
                404: `Apparel not found`,
            },
        });
    }
    /**
     * Partially update a apparel
     * Partially updates an existing apparel identified by its ID. Only the provided fields will be updated.
     * @param id ID of the apparel to operate on
     * @param requestBody Partial apparel object with only the fields to update
     * @returns ApparelDto Successfully updated the apparel
     * @throws ApiError
     */
    public static patchApparel(
        id: number,
        requestBody: ApparelPatchDto,
    ): CancelablePromise<ApparelDto> {
        return __request(OpenAPI, {
            method: 'PATCH',
            url: '/api/v1/apparels/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request`,
                401: `Unauthorized`,
                404: `Apparel not found`,
            },
        });
    }
    /**
     * Delete a apparel
     * Deletes a apparel identified by its ID.
     * @param id ID of the apparel to operate on
     * @returns void
     * @throws ApiError
     */
    public static deleteApparel(
        id: number,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/v1/apparels/{id}',
            path: {
                'id': id,
            },
            errors: {
                400: `Bad request`,
                401: `Unauthorized`,
                404: `Apparel not found`,
            },
        });
    }
}
