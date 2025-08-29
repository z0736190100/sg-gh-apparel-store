import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Plus } from 'lucide-react';
import { PageContainer, PageHeader, PageContent } from '@components/layout';
import { DataTable, TableFilters, TableActions, createCommonActions } from '@components/tables';
import { Button } from '@components/ui';
import { useToast, useConfirmationDialog } from '../../hooks';
import apparelOrderService from '../../services/apparelOrderService';
import type { ApparelOrderDto } from '../../api';
import type { Column, FilterValue, SortConfig } from '@components/tables';

/**
 * Apparel order listing page with pagination, filtering, and actions
 */
const ApparelOrderListPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { success, error } = useToast();
  const { confirmDelete } = useConfirmationDialog();

  const [apparelOrders, setApparelOrders] = useState<ApparelOrderDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [pagination, setPagination] = useState({
    page: 1,
    pageSize: 20,
    total: 0,
  });
  const [filters, setFilters] = useState<FilterValue[]>([]);
  const [sortConfig, setSortConfig] = useState<SortConfig>({
    key: 'createdDate',
    direction: 'desc',
  });

  // Get customerId from URL query params if it exists
  const customerId = searchParams.get('customerId');

  // Load apparel orders data
  const loadApparelOrders = useCallback(async () => {
    setLoading(true);
    try {
      const response = await apparelOrderService.getApparelOrders();

      setApparelOrders(response || []);
      setPagination(prev => ({
        ...prev,
        total: response?.length || 0,
      }));
    } catch (err) {
      error('Failed to load apparel orders');
      console.error('Error loading apparel orders:', err);
    } finally {
      setLoading(false);
    }
  }, [error]);

  // Load data on component mount and when dependencies change
  useEffect(() => {
    loadApparelOrders();
  }, [loadApparelOrders]);

  // Handle page change
  const handlePageChange = (page: number) => {
    setPagination(prev => ({ ...prev, page }));
  };

  // Handle page size change
  const handlePageSizeChange = (pageSize: number) => {
    setPagination(prev => ({ ...prev, pageSize, page: 1 }));
  };

  // Handle filters change
  const handleFiltersChange = (newFilters: FilterValue[]) => {
    setFilters(newFilters);
    setPagination(prev => ({ ...prev, page: 1 })); // Reset to first page
  };

  // Handle apparel order deletion
  const handleDeleteApparelOrder = async (apparelOrder: ApparelOrderDto) => {
    confirmDelete(`Order #${apparelOrder.id}`, async () => {
      try {
        await apparelOrderService.deleteApparelOrder(apparelOrder.id!);
        success(`Apparel order #${apparelOrder.id} deleted successfully`);
        loadApparelOrders(); // Reload the list
      } catch (err) {
        error('Failed to delete apparel order');
        console.error('Error deleting apparel order:', err);
      }
    });
  };

  // Define table columns
  const columns: Column<ApparelOrderDto>[] = [
    {
      key: 'id',
      header: 'Order ID',
      sortable: true,
      render: (value, order) => (
        <div className="font-medium">
          <button
            onClick={() => navigate(`/apparel-orders/${order.id}`)}
            className="text-primary hover:underline"
          >
            #{String(value)}
          </button>
        </div>
      ),
    },
    {
      key: 'customerRef',
      header: 'Customer',
      sortable: true,
      render: (value, order) => (
        <div>
          <div className="font-medium">{order.customerRef || 'Unknown'}</div>
          <div className="text-sm text-gray-500">{String(value)}</div>
        </div>
      ),
    },
    {
      key: 'orderStatus',
      header: 'Status',
      sortable: true,
      render: value => {
        const statusColors = {
          NEW: 'bg-blue-100 text-blue-800',
          VALIDATED: 'bg-yellow-100 text-yellow-800',
          ALLOCATION_PENDING: 'bg-orange-100 text-orange-800',
          ALLOCATED: 'bg-purple-100 text-purple-800',
          PICKED_UP: 'bg-indigo-100 text-indigo-800',
          DELIVERED: 'bg-green-100 text-green-800',
          DELIVERY_EXCEPTION: 'bg-red-100 text-red-800',
          CANCELLED: 'bg-gray-100 text-gray-800',
        };
        const colorClass =
          statusColors[value as keyof typeof statusColors] || 'bg-gray-100 text-gray-800';

        return (
          <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${colorClass}`}>
            {String(value)}
          </span>
        );
      },
    },
    {
      key: 'paymentAmount',
      header: 'Amount',
      sortable: true,
      align: 'right',
      render: value => (value ? `$${Number(value).toFixed(2)}` : '-'),
    },
    {
      key: 'createdDate',
      header: 'Created',
      sortable: true,
      render: value => (value ? new Date(String(value)).toLocaleDateString() : '-'),
    },
    {
      key: 'actions',
      header: 'Actions',
      sortable: false,
      width: '100px',
      align: 'center',
      render: (_, order) => (
        <TableActions
          row={order}
          actions={createCommonActions({
            onView: (order: ApparelOrderDto) => navigate(`/apparel-orders/${order.id}`),
            onEdit: (order: ApparelOrderDto) => navigate(`/apparel-orders/${order.id}/edit`),
            onDelete: (order: ApparelOrderDto) => handleDeleteApparelOrder(order),
          })}
        />
      ),
    },
  ];

  // Define filter configuration
  const filterConfig = [
    {
      key: 'orderStatus',
      label: 'Order Status',
      type: 'select' as const,
      options: [
        { value: 'NEW', label: 'New' },
        { value: 'VALIDATED', label: 'Validated' },
        { value: 'ALLOCATION_PENDING', label: 'Allocation Pending' },
        { value: 'ALLOCATED', label: 'Allocated' },
        { value: 'PICKED_UP', label: 'Picked Up' },
        { value: 'DELIVERED', label: 'Delivered' },
        { value: 'DELIVERY_EXCEPTION', label: 'Delivery Exception' },
        { value: 'CANCELLED', label: 'Cancelled' },
      ],
    },
  ];

  return (
    <PageContainer>
      <PageHeader
        title={customerId ? `Apparel Orders for Customer ${customerId}` : 'Apparel Orders'}
        subtitle={
          customerId ? 'Manage apparel orders for this customer' : 'Manage your apparel order inventory'
        }
        actions={
          <div className="flex gap-2">
            {customerId && (
              <Button variant="outline" onClick={() => navigate('/apparel-orders')}>
                Show All Orders
              </Button>
            )}
            <Button onClick={() => navigate('/apparel-orders/new')}>
              <Plus className="h-4 w-4 mr-2" />
              Create Order
            </Button>
          </div>
        }
      />

      <PageContent>
        <div className="space-y-4">
          {/* Filters */}
          <TableFilters
            filters={filterConfig}
            values={filters}
            onFiltersChange={handleFiltersChange}
            searchPlaceholder="Search orders..."
          />

          {/* Data Table */}
          <DataTable
            data={apparelOrders}
            columns={columns}
            loading={loading}
            sortConfig={sortConfig}
            onSort={setSortConfig}
            pagination={pagination}
            onPageChange={handlePageChange}
            onPageSizeChange={handlePageSizeChange}
            emptyMessage="No apparel orders found. Create your first order to get started."
          />
        </div>
      </PageContent>
    </PageContainer>
  );
};

export default ApparelOrderListPage;
