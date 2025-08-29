import { useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { PageContainer, PageHeader, PageContent } from '@components/layout';
import {
  FormWrapper,
  FormField,
  FormActions,
  CurrencyInput,
  NumberInput,
  SelectInput,
  ImageUpload,
} from '@components/forms';
import { Input } from '@components/ui';
import { Button } from '@components/ui';
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
  imageUrl: string | undefined;
}

/**
 * Apparel Create page component
 * Allows users to create a new apparel with validation and error handling
 */
const ApparelCreatePage: React.FC = () => {
  const navigate = useNavigate();
  const { success, error } = useToast();

  const initialValues: ApparelFormData = {
    apparelName: '',
    apparelStyle: '',
    upc: '',
    price: undefined,
    quantityOnHand: undefined,
    imageUrl: undefined,
  };

  const { values, errors, isValid, isSubmitting, setValue, handleSubmit } = useForm({
    initialValues,
    validationRules: {
      apparelName: apparelValidationRules.apparelName,
      apparelStyle: apparelValidationRules.apparelStyle,
      price: apparelValidationRules.price,
      quantityOnHand: apparelValidationRules.quantityOnHand,
    },
    onSubmit: async (formData: ApparelFormData) => {
      try {
        const apparelData: Omit<ApparelDto, 'id' | 'version' | 'createdDate' | 'updateDate'> = {
          apparelName: formData.apparelName,
          apparelStyle: formData.apparelStyle,
          upc: formData.upc || '',
          price: formData.price || 0,
          quantityOnHand: formData.quantityOnHand || 0,
          description: undefined,
        };

        const createdApparel = await apparelService.createApparel(apparelData);
        success(`Apparel "${createdApparel.apparelName}" created successfully`);
        navigate(`/apparels/${createdApparel.id}`);
      } catch (err) {
        error('Failed to create apparel. Please try again.');
        console.error('Error creating apparel:', err);
      }
    },
  });

  const handleCancel = () => {
    navigate('/apparels');
  };

  // Apparel style options
  const apparelStyleOptions = [
    { value: 'IPA', label: 'IPA' },
    { value: 'Lager', label: 'Lager' },
    { value: 'Stout', label: 'Stout' },
    { value: 'Porter', label: 'Porter' },
    { value: 'Wheat', label: 'Wheat' },
    { value: 'Pilsner', label: 'Pilsner' },
    { value: 'Ale', label: 'Ale' },
    { value: 'Pale Ale', label: 'Pale Ale' },
  ];

  return (
    <PageContainer>
      <PageHeader
        title="Create New Apparel"
        subtitle="Add a new apparel to your inventory"
        actions={
          <Button variant="outline" onClick={handleCancel}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            Back to Apparels
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

          {/* Apparel Image */}
          <FormField
            label="Apparel Image"
            helpText="Upload an image for this apparel (optional)"
            htmlFor="imageUrl"
          >
            <ImageUpload
              value={values.imageUrl}
              onChange={value => setValue('imageUrl', value)}
              placeholder="Upload apparel image"
            />
          </FormField>

          <FormActions
            submitLabel="Create Apparel"
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

export default ApparelCreatePage;
