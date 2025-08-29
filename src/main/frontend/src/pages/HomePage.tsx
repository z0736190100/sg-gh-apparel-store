import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@components/ui';

/**
 * Home page component
 * Displays a welcome message and overview of the application
 */
const HomePage = () => {
  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold">Welcome to Apparel Service</h1>
      <p className="text-muted-foreground">
        Manage your apparel inventory, customers, and orders with ease.
      </p>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <Card>
          <CardHeader>
            <CardTitle>Apparel Management</CardTitle>
            <CardDescription>Manage your apparel inventory</CardDescription>
          </CardHeader>
          <CardContent>
            <p>View, create, update, and delete apparels in your inventory.</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Customer Management</CardTitle>
            <CardDescription>Manage your customers</CardDescription>
          </CardHeader>
          <CardContent>
            <p>View, create, update, and delete customer information.</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Apparel Order Management</CardTitle>
            <CardDescription>Manage apparel orders</CardDescription>
          </CardHeader>
          <CardContent>
            <p>View, create, update, and track apparel orders and shipments.</p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default HomePage;
