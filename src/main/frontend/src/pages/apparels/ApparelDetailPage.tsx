import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Edit, Trash2, ArrowLeft, Package, DollarSign, Calendar, BarChart3 } from 'lucide-react';
import { PageContainer, PageHeader, PageContent } from '@components/layout';
import { TabNavigation } from '@components/navigation';
import { LoadingSpinner } from '@components/dialogs';
import { Button } from '@components/ui';
import { useToast, useConfirmationDialog, useTabs } from '../../hooks';
import apparelService from '../../services/apparelService';
import type { ApparelDto } from '../../api';
import type { Tab } from '@components/navigation';

/**
 * Apparel Detail page component
 * Displays detailed information about a specific apparel with tabs
 */
const ApparelDetailPage: React.FC = () => {
  const { apparelId } = useParams<{ apparelId: string }>();
  const navigate = useNavigate();
  const { success, error } = useToast();
  const { confirmDelete } = useConfirmationDialog();

  const [apparel, setApparel] = useState<ApparelDto | null>(null);
  const [loading, setLoading] = useState(true);

  // Tab management
  const tabIds = ['details', 'inventory', 'history'];
  const { activeTab, changeTab } = useTabs(tabIds);

  // Load apparel data
  const loadApparel = useCallback(async () => {
    if (!apparelId) return;

    setLoading(true);
    try {
      const response = await apparelService.getApparelById(Number(apparelId));
      setApparel(response);
    } catch (err) {
      error('Failed to load apparel details');
      console.error('Error loading apparel:', err);
    } finally {
      setLoading(false);
    }
  }, [apparelId, error]);

  useEffect(() => {
    loadApparel();
  }, [loadApparel]);

  // Handle apparel deletion
  const handleDeleteApparel = async () => {
    if (!apparel) return;

    confirmDelete(apparel.apparelName || 'this apparel', async () => {
      try {
        await apparelService.deleteApparel(apparel.id!);
        success(`Apparel "${apparel.apparelName}" deleted successfully`);
        navigate('/apparels');
      } catch (err) {
        error('Failed to delete apparel');
        console.error('Error deleting apparel:', err);
      }
    });
  };

  // Render apparel details tab
  const renderDetailsTab = () => (
    <div className="grid gap-6 md:grid-cols-2">
      <div className="space-y-4">
        <h3 className="text-lg font-semibold flex items-center gap-2">
          <Package className="h-5 w-5" />
          Basic Information
        </h3>
        <div className="space-y-3">
          <div className="flex justify-between py-2 border-b">
            <span className="font-medium text-gray-600">Apparel Name:</span>
            <span className="font-semibold">{apparel?.apparelName || '-'}</span>
          </div>
          <div className="flex justify-between py-2 border-b">
            <span className="font-medium text-gray-600">Style:</span>
            <span>{apparel?.apparelStyle || '-'}</span>
          </div>
          <div className="flex justify-between py-2 border-b">
            <span className="font-medium text-gray-600">UPC:</span>
            <span className="font-mono text-sm">{apparel?.upc || '-'}</span>
          </div>
          <div className="flex justify-between py-2 border-b">
            <span className="font-medium text-gray-600">Version:</span>
            <span>{apparel?.version || '-'}</span>
          </div>
        </div>
      </div>

      <div className="space-y-4">
        <h3 className="text-lg font-semibold flex items-center gap-2">
          <DollarSign className="h-5 w-5" />
          Pricing & Inventory
        </h3>
        <div className="space-y-3">
          <div className="flex justify-between py-2 border-b">
            <span className="font-medium text-gray-600">Price:</span>
            <span className="font-semibold text-green-600">
              {apparel?.price ? `$${Number(apparel.price).toFixed(2)}` : '-'}
            </span>
          </div>
          <div className="flex justify-between py-2 border-b">
            <span className="font-medium text-gray-600">Quantity on Hand:</span>
            <span className="font-semibold">{apparel?.quantityOnHand?.toLocaleString() || '0'}</span>
          </div>
        </div>
      </div>
    </div>
  );

  // Render inventory tab
  const renderInventoryTab = () => (
    <div className="space-y-6">
      <h3 className="text-lg font-semibold flex items-center gap-2">
        <BarChart3 className="h-5 w-5" />
        Inventory Management
      </h3>
      <div className="grid gap-4 md:grid-cols-3">
        <div className="bg-blue-50 p-4 rounded-lg">
          <div className="text-2xl font-bold text-blue-600">
            {apparel?.quantityOnHand?.toLocaleString() || '0'}
          </div>
          <div className="text-sm text-blue-600">Units in Stock</div>
        </div>
        <div className="bg-green-50 p-4 rounded-lg">
          <div className="text-2xl font-bold text-green-600">
            {apparel?.price
              ? `$${(Number(apparel.price) * (apparel.quantityOnHand || 0)).toFixed(2)}`
              : '$0.00'}
          </div>
          <div className="text-sm text-green-600">Total Value</div>
        </div>
        <div className="bg-yellow-50 p-4 rounded-lg">
          <div className="text-2xl font-bold text-yellow-600">
            {apparel?.quantityOnHand && apparel.quantityOnHand < 50 ? 'Low' : 'Good'}
          </div>
          <div className="text-sm text-yellow-600">Stock Status</div>
        </div>
      </div>
    </div>
  );

  // Render history tab
  const renderHistoryTab = () => (
    <div className="space-y-6">
      <h3 className="text-lg font-semibold flex items-center gap-2">
        <Calendar className="h-5 w-5" />
        Apparel History
      </h3>
      <div className="space-y-3">
        <div className="flex justify-between py-2 border-b">
          <span className="font-medium text-gray-600">Created Date:</span>
          <span>{apparel?.createdDate ? new Date(apparel.createdDate).toLocaleString() : '-'}</span>
        </div>
        <div className="flex justify-between py-2 border-b">
          <span className="font-medium text-gray-600">Last Updated:</span>
          <span>{apparel?.updateDate ? new Date(apparel.updateDate).toLocaleString() : '-'}</span>
        </div>
      </div>
    </div>
  );

  // Define tabs
  const tabs: Tab[] = [
    {
      id: 'details',
      label: 'Details',
      icon: Package,
      content: renderDetailsTab(),
    },
    {
      id: 'inventory',
      label: 'Inventory',
      icon: BarChart3,
      content: renderInventoryTab(),
    },
    {
      id: 'history',
      label: 'History',
      icon: Calendar,
      content: renderHistoryTab(),
    },
  ];

  if (loading) {
    return (
      <PageContainer>
        <LoadingSpinner size="lg" message="Loading apparel details..." centered />
      </PageContainer>
    );
  }

  if (!apparel) {
    return (
      <PageContainer>
        <PageContent>
          <div className="text-center py-8">
            <h2 className="text-xl font-semibold text-gray-900">Apparel not found</h2>
            <p className="text-gray-600 mt-2">The apparel you're looking for doesn't exist.</p>
            <Button onClick={() => navigate('/apparels')} className="mt-4">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Back to Apparels
            </Button>
          </div>
        </PageContent>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader
        title={apparel.apparelName || 'Apparel Details'}
        subtitle={`${apparel.apparelStyle || 'Unknown Style'} • ID: ${apparel.id}`}
        actions={
          <div className="flex gap-2">
            <Button variant="outline" onClick={() => navigate('/apparels')}>
              <ArrowLeft className="h-4 w-4 mr-2" />
              Back
            </Button>
            <Button onClick={() => navigate(`/apparels/${apparel.id}/edit`)}>
              <Edit className="h-4 w-4 mr-2" />
              Edit
            </Button>
            <Button variant="destructive" onClick={handleDeleteApparel}>
              <Trash2 className="h-4 w-4 mr-2" />
              Delete
            </Button>
          </div>
        }
      />

      <PageContent>
        <TabNavigation
          tabs={tabs}
          activeTab={activeTab}
          onTabChange={changeTab}
          variant="underline"
        />
      </PageContent>
    </PageContainer>
  );
};

export default ApparelDetailPage;
