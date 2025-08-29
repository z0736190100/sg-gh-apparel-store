import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { PageContainer, PageHeader, PageContent } from '@components/layout';
import {
  FormWrapper,
  FormField,
  FormActions,
  CurrencyInput,
  NumberInput,
  SelectInput,
} from '@components/forms';
import { Input, Button } from '@components/ui';
import { LoadingSpinner } from '@components/dialogs';
import { useForm, useToast } from '../../hooks';
import { apparelValidationRules } from '../../utils/validation';
import apparelService from '../../services/apparelService';
import type { ApparelDto } from '../../api';

interface ApparelFormData extends Record<string, unknown> {
  apparelName: string;
  apparelStyle: string;
  upc: string;
  price: number | undefined;
  quantityOnHand: number | undefined;
}

/**
 * Apparel Edit page component
 * Allows users to edit an existing apparel with validation and error handling
 */
const ApparelEditPage: React.FC = () => {
  const navigate = useNavigate();
  const { apparelId } = useParams<{ apparelId: string }>();
  const { success, error } = useToast();
  const [initialLoading, setInitialLoading] = useState(true);
  const [apparel, setApparel] = useState<ApparelDto | null>(null);

  const initialValues: ApparelFormData = {
    apparelName: '',
    apparelStyle: '',
    upc: '',
    price: undefined,
    quantityOnHand: undefined,
  };

  const { values, errors, isValid, isSubmitting, setValue, setValues, handleSubmit } = useForm({
    initialValues,
    validationRules: {
      apparelName: apparelValidationRules.apparelName,
      apparelStyle: apparelValidationRules.apparelStyle,
      price: apparelValidationRules.price,
      quantityOnHand: apparelValidationRules.quantityOnHand,
    },
    onSubmit: async (formData: ApparelFormData) => {
      if (!apparel) return;

      try {
        const apparelData: Omit<ApparelDto, 'id' | 'version' | 'createdDate' | 'updateDate'> = {
          apparelName: formData.apparelName,
          apparelStyle: formData.apparelStyle,
          upc: formData.upc || '',
          price: formData.price || 0,
          quantityOnHand: formData.quantityOnHand || 0,
          description: apparel?.description,
        };

        const updatedApparel = await apparelService.updateApparel(apparel.id!, apparelData);

        // Optimistic update - update local state immediately
        setApparel(updatedApparel);

        success(`Apparel "${updatedApparel.apparelName}" updated successfully`);
        navigate(`/apparels/${updatedApparel.id}`);
      } catch (err) {
        error('Failed to update apparel. Please try again.');
        console.error('Error updating apparel:', err);
      }
    },
  });

  // Load apparel data on component mount
  useEffect(() => {
    const loadApparel = async () => {
      if (!apparelId) {
        error('Apparel ID is required');
        navigate('/apparels');
        return;
      }

      setInitialLoading(true);
      try {
        const apparelData = await apparelService.getApparelById(Number(apparelId));
        setApparel(apparelData);

        // Pre-populate form with existing apparel data
        setValues({
          apparelName: apparelData.apparelName || '',
          apparelStyle: apparelData.apparelStyle || '',
          upc: apparelData.upc || '',
          price: apparelData.price,
          quantityOnHand: apparelData.quantityOnHand,
        });
      } catch (err) {
        error('Failed to load apparel data');
        console.error('Error loading apparel:', err);
        navigate('/apparels');
      } finally {
        setInitialLoading(false);
      }
    };

    loadApparel();
  }, [apparelId, navigate, error, setValues]);

  const handleCancel = () => {
    navigate(`/apparels/${apparelId}`);
  };

  // Apparel style options
  const apparelStyleOptions = [
    { value: 'Loose', label: 'Loose' },
    { value: 'Oversize', label: 'Oversize' },
    { value: 'Fit', label: 'Fit' },
    { value: 'Stretch', label: 'Stretch' },
  ];

  if (initialLoading) {
    return (
      <PageContainer>
        <LoadingSpinner size="lg" message="Loading apparel data..." centered />
      </PageContainer>
    );
  }

  if (!apparel) {
    return (
      <PageContainer>
        <PageContent>
          <div className="text-center py-8">
            <h2 className="text-xl font-semibold text-gray-900">Apparel not found</h2>
            <p className="text-gray-600 mt-2">The apparel you're trying to edit doesn't exist.</p>
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
        title="Edit Apparel"
        subtitle={`Update "${apparel.apparelName}" information`}
        actions={
          <Button variant="outline" onClick={handleCancel}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            Back to Details
          </Button>
        }
      />

      <PageContent>
        <FormWrapper onSubmit={handleSubmit} isLoading={isSubmitting}>
          <div className="grid gap-6 md:grid-cols-2">
            {/* Apparel Name */}
            <FormField label="Apparel Name" required error={errors.apparelName?.[0]} htmlFor="apparelName">
              <Input
                id="apparelName"
                placeholder="Enter apparel name"
                value={values.apparelName}
                onChange={e => setValue('apparelName', e.target.value)}
              />
            </FormField>

            {/* Apparel Style */}
            <FormField
              label="Apparel Style"
              required
              error={errors.apparelStyle?.[0]}
              htmlFor="apparelStyle"
            >
              <SelectInput
                value={values.apparelStyle}
                onChange={value => setValue('apparelStyle', value)}
                options={apparelStyleOptions}
                placeholder="Select a apparel style"
              />
            </FormField>

            {/* UPC */}
            <FormField label="UPC" helpText="Universal Product Code (optional)" htmlFor="upc">
              <Input
                id="upc"
                placeholder="Enter UPC"
                value={values.upc}
                onChange={e => setValue('upc', e.target.value)}
              />
            </FormField>

            {/* Price */}
            <FormField label="Price" required error={errors.price?.[0]} htmlFor="price">
              <CurrencyInput
                id="price"
                value={values.price}
                onChange={value => setValue('price', value)}
                placeholder="0.00"
                min={0}
              />
            </FormField>

            {/* Quantity on Hand */}
            <FormField
              label="Quantity on Hand"
              helpText="Current inventory count"
              error={errors.quantityOnHand?.[0]}
              htmlFor="quantityOnHand"
            >
              <NumberInput
                id="quantityOnHand"
                value={values.quantityOnHand}
                onChange={value => setValue('quantityOnHand', value)}
                placeholder="0"
                min={0}
                allowDecimals={false}
                allowNegative={false}
              />
            </FormField>
          </div>

          <FormActions
            submitLabel="Update Apparel"
            cancelLabel="Cancel"
            onCancel={handleCancel}
            isLoading={isSubmitting}
            submitDisabled={!isValid}
          />
        </FormWrapper>
      </PageContent>
    </PageContainer>
  );
};

export default ApparelEditPage;
