import React, { Suspense } from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { RootLayout } from './layouts';
import { AuthProvider } from './contexts/AuthContext';
import { ToastProvider } from './contexts/ToastContext';
import './App.css';

// Lazy load page components for code splitting
const HomePage = React.lazy(() => import('./pages/HomePage'));
const LoginPage = React.lazy(() => import('./pages/auth/LoginPage'));

// Apparel pages
const ApparelListPage = React.lazy(() => import('./pages/apparels/ApparelListPage'));
const ApparelDetailPage = React.lazy(() => import('./pages/apparels/ApparelDetailPage'));
const ApparelCreatePage = React.lazy(() => import('./pages/apparels/ApparelCreatePage'));
const ApparelEditPage = React.lazy(() => import('./pages/apparels/ApparelEditPage'));

// Customer pages
const CustomerListPage = React.lazy(() => import('./pages/customers/CustomerListPage'));
const CustomerDetailPage = React.lazy(() => import('./pages/customers/CustomerDetailPage'));
const CustomerCreatePage = React.lazy(() => import('./pages/customers/CustomerCreatePage'));
const CustomerEditPage = React.lazy(() => import('./pages/customers/CustomerEditPage'));

// Apparel Order pages
const ApparelOrderListPage = React.lazy(() => import('./pages/apparelOrders/ApparelOrderListPage'));
const ApparelOrderDetailPage = React.lazy(() => import('./pages/apparelOrders/ApparelOrderDetailPage'));
const ApparelOrderCreatePage = React.lazy(() => import('./pages/apparelOrders/ApparelOrderCreatePage'));
const ApparelOrderEditPage = React.lazy(() => import('./pages/apparelOrders/ApparelOrderEditPage'));
const ApparelOrderShipmentCreatePage = React.lazy(
  () => import('./pages/apparelOrders/ApparelOrderShipmentCreatePage')
);

// Loading component for Suspense fallback
const LoadingSpinner: React.FC = () => (
  <div className="flex items-center justify-center min-h-screen">
    <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary"></div>
  </div>
);

/**
 * Main application component
 * Sets up the routing structure for the application
 */
const App: React.FC = () => {
  // Create router configuration
  const router = createBrowserRouter([
    {
      path: '/login',
      element: (
        <Suspense fallback={<LoadingSpinner />}>
          <LoginPage />
        </Suspense>
      ),
    },
    {
      path: '/',
      element: <RootLayout />,
      children: [
        {
          index: true,
          element: (
            <Suspense fallback={<LoadingSpinner />}>
              <HomePage />
            </Suspense>
          ),
        },
        {
          path: 'apparels',
          children: [
            {
              index: true,
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelListPage />
                </Suspense>
              ),
            },
            {
              path: 'new',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelCreatePage />
                </Suspense>
              ),
            },
            {
              path: ':apparelId',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelDetailPage />
                </Suspense>
              ),
            },
            {
              path: ':apparelId/edit',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelEditPage />
                </Suspense>
              ),
            },
          ],
        },
        {
          path: 'customers',
          children: [
            {
              index: true,
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <CustomerListPage />
                </Suspense>
              ),
            },
            {
              path: 'new',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <CustomerCreatePage />
                </Suspense>
              ),
            },
            {
              path: ':customerId',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <CustomerDetailPage />
                </Suspense>
              ),
            },
            {
              path: ':customerId/edit',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <CustomerEditPage />
                </Suspense>
              ),
            },
          ],
        },
        {
          path: 'apparel-orders',
          children: [
            {
              index: true,
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelOrderListPage />
                </Suspense>
              ),
            },
            {
              path: 'new',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelOrderCreatePage />
                </Suspense>
              ),
            },
            {
              path: ':apparelOrderId',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelOrderDetailPage />
                </Suspense>
              ),
            },
            {
              path: ':apparelOrderId/edit',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelOrderEditPage />
                </Suspense>
              ),
            },
            {
              path: ':apparelOrderId/shipments/new',
              element: (
                <Suspense fallback={<LoadingSpinner />}>
                  <ApparelOrderShipmentCreatePage />
                </Suspense>
              ),
            },
          ],
        },
      ],
    },
  ]);

  return (
    <AuthProvider>
      <ToastProvider>
        <RouterProvider router={router} />
      </ToastProvider>
    </AuthProvider>
  );
};

export default App;
