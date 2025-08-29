import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus } from 'lucide-react';
import { PageContainer, PageHeader, PageContent } from '@components/layout';
import { DataTable, TableFilters, TableActions, createCommonActions } from '@components/tables';
import { Button } from '@components/ui';
import { useToast, useConfirmationDialog } from '../../hooks';
import apparelService from '../../services/apparelService';
import type { ApparelDto } from '../../api';
import type { Column, FilterValue, SortConfig } from '@components/tables';

/**
 * Apparel listing page with pagination, filtering, and actions
 */
const ApparelListPage: React.FC = () => {
  const navigate = useNavigate();
  const { success, error } = useToast();
  const { confirmDelete } = useConfirmationDialog();

  const [apparels, setApparels] = useState<ApparelDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [pagination, setPagination] = useState({
    page: 1,
    pageSize: 20,
    total: 0,
  });
  const [filters, setFilters] = useState<FilterValue[]>([]);
  const [sortConfig, setSortConfig] = useState<SortConfig>({ key: 'apparelName', direction: 'asc' });

  // Load apparels data
  const loadApparels = useCallback(async () => {
    setLoading(true);
    try {
      // Extract filter values
      const searchFilter = filters.find(f => f.key === 'search');
      const styleFilter = filters.find(f => f.key === 'apparelStyle');

      const response = await apparelService.getApparels(
        { page: pagination.page - 1, size: pagination.pageSize }, // API uses 0-based pagination
        { sort: sortConfig.key, direction: sortConfig.direction },
        searchFilter?.value ? String(searchFilter.value) : undefined,
        styleFilter?.value ? String(styleFilter.value) : undefined
      );

      setApparels(response.content || []);
      setPagination(prev => ({
        ...prev,
        total: response.totalElements || 0,
      }));
    } catch (err) {
      error('Failed to load apparels');
      console.error('Error loading apparels:', err);
    } finally {
      setLoading(false);
    }
  }, [pagination.page, pagination.pageSize, filters, sortConfig, error]);

  // Load data on component mount and when dependencies change
  useEffect(() => {
    loadApparels();
  }, [loadApparels]);

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

  // Handle apparel deletion
  const handleDeleteApparel = async (apparel: ApparelDto) => {
    confirmDelete(apparel.apparelName || 'this apparel', async () => {
      try {
        await apparelService.deleteApparel(apparel.id!);
        success(`Apparel "${apparel.apparelName}" deleted successfully`);
        loadApparels(); // Reload the list
      } catch (err) {
        error('Failed to delete apparel');
        console.error('Error deleting apparel:', err);
      }
    });
  };

  // Define table columns
  const columns: Column<ApparelDto>[] = [
    {
      key: 'apparelName',
      header: 'Name',
      sortable: true,
      render: (value, apparel) => (
        <div className="font-medium">
          <button
            onClick={() => navigate(`/apparels/${apparel.id}`)}
            className="text-primary hover:underline"
          >
            {String(value)}
          </button>
        </div>
      ),
    },
    {
      key: 'apparelStyle',
      header: 'Style',
      sortable: true,
    },
    {
      key: 'price',
      header: 'Price',
      sortable: true,
      align: 'right',
      render: value => (value ? `$${Number(value).toFixed(2)}` : '-'),
    },
    {
      key: 'quantityOnHand',
      header: 'Quantity',
      sortable: true,
      align: 'right',
      render: value => value?.toLocaleString() || '0',
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
      render: (_, apparel) => (
        <TableActions
          row={apparel}
          actions={createCommonActions({
            onView: (apparel: ApparelDto) => navigate(`/apparels/${apparel.id}`),
            onEdit: (apparel: ApparelDto) => navigate(`/apparels/${apparel.id}/edit`),
            onDelete: (apparel: ApparelDto) => handleDeleteApparel(apparel),
          })}
        />
      ),
    },
  ];

  // Define filter configuration
  const filterConfig = [
    {
      key: 'apparelStyle',
      label: 'Apparel Style',
      type: 'select' as const,
      options: [
        { value: 'Loose', label: 'Loose' },
        { value: 'Oversize', label: 'Oversize' },
        { value: 'Fit', label: 'Fit' },
        { value: 'Stretch', label: 'Stretch' },
      ],
    },
  ];

  return (
    <PageContainer>
      <PageHeader
        title="Apparels"
        subtitle="Manage your apparel inventory"
        actions={
          <Button onClick={() => navigate('/apparels/new')}>
            <Plus className="h-4 w-4 mr-2" />
            Add Apparel
          </Button>
        }
      />

      <PageContent>
        <div className="space-y-4">
          {/* Filters */}
          <TableFilters
            filters={filterConfig}
            values={filters}
            onFiltersChange={handleFiltersChange}
            searchPlaceholder="Search apparels..."
          />

          {/* Data Table */}
          <DataTable
            data={apparels}
            columns={columns}
            loading={loading}
            sortConfig={sortConfig}
            onSort={setSortConfig}
            pagination={pagination}
            onPageChange={handlePageChange}
            onPageSizeChange={handlePageSizeChange}
            emptyMessage="No apparels found. Create your first apparel to get started."
          />
        </div>
      </PageContent>
    </PageContainer>
  );
};

export default ApparelListPage;
