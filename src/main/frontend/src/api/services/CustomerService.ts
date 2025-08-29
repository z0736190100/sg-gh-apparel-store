/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CustomerDto } from '../models/CustomerDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class CustomerService {
    /**
     * Get all customers
     * Returns a list of all customers in the system.
     * @returns CustomerDto Successfully retrieved the list of customers
     * @throws ApiError
     */
    public static getAllCustomers(): CancelablePromise<Array<CustomerDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/v1/customers',
            errors: {
                401: `Unauthorized`,
            },
        });
    }
    /**
     * Create a new customer
     * Creates a new customer in the system. The ID, version, createdDate, and updateDate fields will be ignored if provided.
     * @param requestBody Customer object to be created
     * @returns CustomerDto Successfully created a new customer
     * @throws ApiError
     */
    public static createCustomer(
        requestBody: CustomerDto,
    ): CancelablePromise<CustomerDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/v1/customers',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request - validation error`,
                401: `Unauthorized`,
            },
        });
    }
    /**
     * Get a customer by ID
     * Returns a single customer by ID.
     * @param id ID of the customer to operate on
     * @returns CustomerDto Successfully retrieved the customer
     * @throws ApiError
     */
    public static getCustomerById(
        id: number,
    ): CancelablePromise<CustomerDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/v1/customers/{id}',
            path: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                404: `Customer not found`,
            },
        });
    }
    /**
     * Update an existing customer
     * Updates an existing customer in the system. The ID in the path must match the ID in the request body.
     * @param id ID of the customer to operate on
     * @param requestBody Customer object with updated fields
     * @returns CustomerDto Successfully updated the customer
     * @throws ApiError
     */
    public static updateCustomer(
        id: number,
        requestBody: CustomerDto,
    ): CancelablePromise<CustomerDto> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/v1/customers/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request - validation error or ID mismatch`,
                401: `Unauthorized`,
                404: `Customer not found`,
            },
        });
    }
    /**
     * Delete a customer
     * Deletes a customer from the system.
     * @param id ID of the customer to operate on
     * @returns void
     * @throws ApiError
     */
    public static deleteCustomer(
        id: number,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/v1/customers/{id}',
            path: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                404: `Customer not found`,
            },
        });
    }
}
