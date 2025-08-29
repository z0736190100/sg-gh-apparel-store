/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApparelOrderDto } from '../models/ApparelOrderDto';
import type { ApparelOrderShipmentDto } from '../models/ApparelOrderShipmentDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ApparelOrderService {
    /**
     * Get all apparel orders
     * Returns a list of all apparel orders in the system.
     * @returns ApparelOrderDto Successfully retrieved the list of apparel orders
     * @throws ApiError
     */
    public static getAllApparelOrders(): CancelablePromise<Array<ApparelOrderDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/v1/apparel-orders',
            errors: {
                401: `Unauthorized`,
            },
        });
    }
    /**
     * Create a new apparel order
     * Creates a new apparel order in the system. The ID, version, createdDate, and updateDate fields will be ignored if provided.
     * @param requestBody Apparel order object to be created
     * @returns ApparelOrderDto Successfully created a new apparel order
     * @throws ApiError
     */
    public static createApparelOrder(
        requestBody: ApparelOrderDto,
    ): CancelablePromise<ApparelOrderDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/v1/apparel-orders',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request - validation error`,
                401: `Unauthorized`,
            },
        });
    }
    /**
     * Get a apparel order by ID
     * Returns a apparel order by its ID.
     * @param id ID of the apparel order
     * @returns ApparelOrderDto Successfully retrieved the apparel order
     * @throws ApiError
     */
    public static getApparelOrderById(
        id: number,
    ): CancelablePromise<ApparelOrderDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/v1/apparel-orders/{id}',
            path: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                404: `Apparel order not found`,
            },
        });
    }
    /**
     * Update a apparel order
     * Updates an existing apparel order. The ID in the path must match the ID in the request body.
     * @param id ID of the apparel order
     * @param requestBody Apparel order object with updated information
     * @returns ApparelOrderDto Successfully updated the apparel order
     * @throws ApiError
     */
    public static updateApparelOrder(
        id: number,
        requestBody: ApparelOrderDto,
    ): CancelablePromise<ApparelOrderDto> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/v1/apparel-orders/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request - validation error`,
                401: `Unauthorized`,
                404: `Apparel order not found`,
            },
        });
    }
    /**
     * Delete a apparel order
     * Deletes a apparel order by its ID.
     * @param id ID of the apparel order
     * @returns void
     * @throws ApiError
     */
    public static deleteApparelOrder(
        id: number,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/v1/apparel-orders/{id}',
            path: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                404: `Apparel order not found`,
            },
        });
    }
    /**
     * Get all shipments for a apparel order
     * Returns a list of all shipments for a specific apparel order.
     * @param apparelOrderId ID of the apparel order to get shipments for
     * @returns ApparelOrderShipmentDto Successfully retrieved the list of shipments
     * @throws ApiError
     */
    public static getAllShipments(
        apparelOrderId: number,
    ): CancelablePromise<Array<ApparelOrderShipmentDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/v1/apparel-orders/{apparelOrderId}/shipments',
            path: {
                'apparelOrderId': apparelOrderId,
            },
            errors: {
                401: `Unauthorized`,
                404: `Apparel order not found`,
            },
        });
    }
    /**
     * Create a new shipment for a apparel order
     * Creates a new shipment for a specific apparel order. The ID, version, createdDate, and updateDate fields will be ignored if provided.
     * @param apparelOrderId ID of the apparel order to create a shipment for
     * @param requestBody Shipment object to be created
     * @returns ApparelOrderShipmentDto Successfully created a new shipment
     * @throws ApiError
     */
    public static createShipment(
        apparelOrderId: number,
        requestBody: ApparelOrderShipmentDto,
    ): CancelablePromise<ApparelOrderShipmentDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/v1/apparel-orders/{apparelOrderId}/shipments',
            path: {
                'apparelOrderId': apparelOrderId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request - validation error`,
                401: `Unauthorized`,
                404: `Apparel order not found`,
            },
        });
    }
    /**
     * Get a specific shipment for a apparel order
     * Returns a specific shipment for a apparel order.
     * @param apparelOrderId ID of the apparel order
     * @param shipmentId ID of the shipment to get
     * @returns ApparelOrderShipmentDto Successfully retrieved the shipment
     * @throws ApiError
     */
    public static getShipmentById(
        apparelOrderId: number,
        shipmentId: number,
    ): CancelablePromise<ApparelOrderShipmentDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/v1/apparel-orders/{apparelOrderId}/shipments/{shipmentId}',
            path: {
                'apparelOrderId': apparelOrderId,
                'shipmentId': shipmentId,
            },
            errors: {
                401: `Unauthorized`,
                404: `Apparel order or shipment not found`,
            },
        });
    }
    /**
     * Update a specific shipment for a apparel order
     * Updates a specific shipment for a apparel order. The ID, version, createdDate, and updateDate fields will be ignored if provided.
     * @param apparelOrderId ID of the apparel order
     * @param shipmentId ID of the shipment to update
     * @param requestBody Shipment object to be updated
     * @returns ApparelOrderShipmentDto Successfully updated the shipment
     * @throws ApiError
     */
    public static updateShipment(
        apparelOrderId: number,
        shipmentId: number,
        requestBody: ApparelOrderShipmentDto,
    ): CancelablePromise<ApparelOrderShipmentDto> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/v1/apparel-orders/{apparelOrderId}/shipments/{shipmentId}',
            path: {
                'apparelOrderId': apparelOrderId,
                'shipmentId': shipmentId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request - validation error`,
                401: `Unauthorized`,
                404: `Apparel order or shipment not found`,
            },
        });
    }
    /**
     * Delete a specific shipment for a apparel order
     * Deletes a specific shipment for a apparel order.
     * @param apparelOrderId ID of the apparel order
     * @param shipmentId ID of the shipment to delete
     * @returns void
     * @throws ApiError
     */
    public static deleteShipment(
        apparelOrderId: number,
        shipmentId: number,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/v1/apparel-orders/{apparelOrderId}/shipments/{shipmentId}',
            path: {
                'apparelOrderId': apparelOrderId,
                'shipmentId': shipmentId,
            },
            errors: {
                401: `Unauthorized`,
                404: `Apparel order or shipment not found`,
            },
        });
    }
}
