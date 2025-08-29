import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  Input,
  Label,
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@components/ui';

// Local interfaces for form data
interface CustomerOption {
  id: number;
  name: string;
  email: string;
}

interface ApparelOption {
  id: number;
  name: string;
  style: string;
  price: number;
  quantityOnHand: number;
}

interface LineItem {
  id: number;
  apparelId: string | number;
  apparelName: string;
  quantity: number;
  price: number;
}

/**
 * Apparel Order Create page component
 * Allows users to create a new apparel order
 */
const ApparelOrderCreatePage: React.FC = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [customers, setCustomers] = useState<CustomerOption[]>([]);
  const [apparels, setApparels] = useState<ApparelOption[]>([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState('');
  const [customerRef, setCustomerRef] = useState('');
  const [lineItems, setLineItems] = useState<LineItem[]>([
    { id: 1, apparelId: '', apparelName: '', quantity: 1, price: 0 },
  ]);

  // Simulate fetching customers and apparels data
  useEffect(() => {
    // In a real application, you would fetch the customers and apparels data from the API
    // For now, we'll use mock data
    const mockCustomers = [
      { id: 1, name: 'John Doe', email: 'john.doe@example.com' },
      { id: 2, name: 'Jane Smith', email: 'jane.smith@example.com' },
      { id: 3, name: 'Bob Johnson', email: 'bob.johnson@example.com' },
    ];

    const mockApparels = [
      { id: 1, name: 'Mango Bobs', style: 'Loose', price: 12.99, quantityOnHand: 100 },
      { id: 2, name: 'Galaxy Cat', style: 'Stretch', price: 11.99, quantityOnHand: 75 },
      { id: 3, name: 'Pinball Fit', style: 'Fit', price: 13.99, quantityOnHand: 50 },
    ];

    // Simulate API call delay
    const timer = setTimeout(() => {
      setCustomers(mockCustomers);
      setApparels(mockApparels);
      setLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, []);

  const handleCustomerChange = (customerId: string) => {
    setSelectedCustomerId(customerId);

    // Generate a customer reference based on the selected customer
    // In a real app, this might be more sophisticated or handled by the backend
    const customer = customers.find(c => c.id.toString() === customerId);
    if (customer) {
      const ref = `CUST-${customer.id}-${Date.now().toString().slice(-4)}`;
      setCustomerRef(ref);
    }
  };

  const handleApparelChange = (apparelId: string, index: number) => {
    const apparel = apparels.find(b => b.id.toString() === apparelId);

    if (apparel) {
      const updatedLineItems = [...lineItems];
      updatedLineItems[index] = {
        ...updatedLineItems[index],
        apparelId: apparel.id,
        apparelName: apparel.name,
        price: apparel.price,
      };
      setLineItems(updatedLineItems);
    }
  };

  const handleQuantityChange = (quantity: string, index: number) => {
    const updatedLineItems = [...lineItems];
    updatedLineItems[index] = {
      ...updatedLineItems[index],
      quantity: parseInt(quantity) || 0,
    };
    setLineItems(updatedLineItems);
  };

  const addLineItem = () => {
    setLineItems([
      ...lineItems,
      { id: lineItems.length + 1, apparelId: '', apparelName: '', quantity: 1, price: 0 },
    ]);
  };

  const removeLineItem = (index: number) => {
    if (lineItems.length > 1) {
      const updatedLineItems = lineItems.filter((_, i) => i !== index);
      setLineItems(updatedLineItems);
    }
  };

  const calculateTotal = () => {
    return lineItems.reduce((total, item) => {
      return total + item.price * item.quantity;
    }, 0);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!selectedCustomerId || lineItems.some(item => !item.apparelId)) {
      alert('Please select a customer and apparel for each line item');
      return;
    }

    setSubmitting(true);

    // In a real application, you would submit the order data to the API
    // For now, we'll just simulate a successful submission
    setTimeout(() => {
      setSubmitting(false);
      navigate('/apparel-orders');
    }, 1000);
  };

  const handleCancel = () => {
    navigate('/apparel-orders');
  };

  if (loading) {
    return (
      <div className="flex h-full items-center justify-center">
        <p className="text-lg">Loading...</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Create New Apparel Order</h1>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Customer Information</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid gap-4 md:grid-cols-2">
              <div className="space-y-2">
                <Label htmlFor="customer">Customer</Label>
                <Select value={selectedCustomerId} onValueChange={handleCustomerChange} required>
                  <SelectTrigger>
                    <SelectValue placeholder="Select a customer" />
                  </SelectTrigger>
                  <SelectContent>
                    {customers.map(customer => (
                      <SelectItem key={customer.id} value={customer.id.toString()}>
                        {customer.name} ({customer.email})
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="customerRef">Customer Reference</Label>
                <Input
                  id="customerRef"
                  value={customerRef}
                  onChange={e => setCustomerRef(e.target.value)}
                  placeholder="Customer reference"
                  disabled={!selectedCustomerId}
                  required
                />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Order Items</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {lineItems.map((item, index) => (
                <div key={index} className="grid gap-4 rounded-md border p-4 md:grid-cols-4">
                  <div className="space-y-2">
                    <Label htmlFor={`apparel-${index}`}>Apparel</Label>
                    <Select
                      value={item.apparelId.toString()}
                      onValueChange={value => handleApparelChange(value, index)}
                      required
                    >
                      <SelectTrigger id={`apparel-${index}`}>
                        <SelectValue placeholder="Select a apparel" />
                      </SelectTrigger>
                      <SelectContent>
                        {apparels.map(apparel => (
                          <SelectItem key={apparel.id} value={apparel.id.toString()}>
                            {apparel.name} ({apparel.style})
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor={`quantity-${index}`}>Quantity</Label>
                    <Input
                      id={`quantity-${index}`}
                      type="number"
                      min="1"
                      value={item.quantity}
                      onChange={e => handleQuantityChange(e.target.value, index)}
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <Label>Price</Label>
                    <div className="flex h-9 items-center rounded-md border border-input bg-background px-3 text-sm">
                      ${item.price.toFixed(2)}
                    </div>
                  </div>
                  <div className="space-y-2">
                    <Label>Subtotal</Label>
                    <div className="flex h-9 items-center rounded-md border border-input bg-background px-3 text-sm">
                      ${(item.price * item.quantity).toFixed(2)}
                    </div>
                  </div>
                  {lineItems.length > 1 && (
                    <div className="flex items-end md:col-span-4">
                      <button
                        type="button"
                        onClick={() => removeLineItem(index)}
                        className="rounded-md bg-red-500 px-3 py-2 text-sm text-white"
                      >
                        Remove Item
                      </button>
                    </div>
                  )}
                </div>
              ))}

              <div className="flex justify-between">
                <button
                  type="button"
                  onClick={addLineItem}
                  className="rounded-md bg-blue-500 px-4 py-2 text-white"
                >
                  Add Item
                </button>
                <div className="text-xl font-bold">Total: ${calculateTotal().toFixed(2)}</div>
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="flex justify-end gap-2">
          <button
            type="button"
            onClick={handleCancel}
            className="rounded-md bg-gray-500 px-4 py-2 text-white"
            disabled={submitting}
          >
            Cancel
          </button>
          <button
            type="submit"
            className="rounded-md bg-primary px-4 py-2 text-primary-foreground"
            disabled={submitting}
          >
            {submitting ? 'Creating Order...' : 'Create Order'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default ApparelOrderCreatePage;
