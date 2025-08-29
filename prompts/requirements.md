# React Frontend Implementation Guide for Spring Boot Apparel Service

## Introduction

This guide provides detailed, step-by-step instructions for implementing a React frontend for the Spring Boot Apparel Service application. The frontend will interact with the existing REST API endpoints to provide a user-friendly interface for managing Apparels, Apparel orders, and customers.

## Project Overview

The Spring Boot application provides REST API endpoints for:
- Apparel inventory management
- Apparel order processing
- Customer management

The React frontend will provide a user interface for these operations, allowing users to:
- View, create, update, and delete Apparels
- View, create, update, and delete Apparel orders
- View, create, update, and delete customers
- Manage Apparel order shipments

## Technologies Used

The React application will use the following technologies:

### Core Technologies
- **React v19.1.0** - UI library
- **React DOM v19.1.0** - React rendering for web
- **React Router Dom 7.6.36** - Routing library for React
- **TypeScript 5.8.3** - Typed superset of JavaScript
- **Vite 6.3.5** - Build tool and development server

### UI Components and Styling
- **Shadcn 2.6.3** - Reusable components built on Radix UI
- **Radix 3.2.1** - Unstyled, accessible UI components
- **Tailwind CSS 4.1.10** - Utility-first CSS framework
- **tw-animate-css 1.3.4** - Tailwind CSS animations
- **tailwind-merge 3.3.1** - Utility for merging Tailwind classes
- **clsx 2.1.1** - Utility for conditional class names
- **class-variance-authority 0.7.1** - Create variant components
- **lucide-react 0.515.0** - Icon library for React

### HTTP and Data Handling
- **Axios 1.10.0** - Promise-based HTTP client

### Development Tools
- **Node.js v22.16.0** - JavaScript runtime
- **npm 11.4.0** - Package manager
- **ESLint** - Code linting
- **Prettier** - Code formatting
- **PostCSS 8.5.5** - CSS transformation tool
- **Autoprefixer 10.4.20** - Add vendor prefixes to CSS

### Testing
- **Jest 30.0.0** - Testing framework
- **React Testing Library 16.3.0** - Testing utilities for React

### Type Definitions
- **@types/react 19.1.0** - TypeScript definitions for React
- **@types/react-dom 19.1.0** - TypeScript definitions for React DOM
- **@types/node v24.0.1** - TypeScript definitions for Node.js
- **@types/jest 29.5.14** - TypeScript definitions for Jest

## Part 1: Project Setup

### Step 1: Create the React Project

1. Navigate to the root of your Spring Boot project:
   ```bash
   cd your-spring-boot-project
   ```

2. Create a new React project using Vite:
   ```bash
   npm create vite@latest src/main/frontend -- --template react-ts
   ```

3. Navigate to the frontend directory:
   ```bash
   cd src/main/frontend
   ```

4. Install dependencies:
   ```bash
   npm install
   ```

### Step 2: Configure Vite

Create or update `vite.config.ts` in the frontend directory:

```typescript
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: '../../resources/static',
    emptyOutDir: true,
    sourcemap: true,
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
});
```

### Step 3: Configure Environment Variables

Create a `.env` file in the frontend directory:

```
VITE_API_BASE_URL=/api/v1
```

Create a `.env.development` file for development-specific settings:

```
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

### Step 4: Install UI and Utility Libraries

```bash
# Install UI libraries
npm install @radix-ui/react-icons @radix-ui/react-dialog @radix-ui/react-dropdown-menu @radix-ui/react-toast @radix-ui/react-tabs @radix-ui/react-select @radix-ui/react-popover

# Install styling libraries
npm install tailwindcss postcss autoprefixer tw-animate-css tailwind-merge clsx class-variance-authority lucide-react

# Install HTTP client
npm install axios

# Install routing
npm install react-router-dom
```

### Step 5: Configure Tailwind CSS

1. Initialize Tailwind CSS:
   ```bash
   npx tailwindcss init -p
   ```

2. Update `tailwind.config.js`:
   ```javascript
   /** @type {import('tailwindcss').Config} */
   export default {
     content: [
       "./index.html",
       "./src/**/*.{js,ts,jsx,tsx}",
     ],
     theme: {
       extend: {
         colors: {
           border: "hsl(var(--border))",
           input: "hsl(var(--input))",
           ring: "hsl(var(--ring))",
           background: "hsl(var(--background))",
           foreground: "hsl(var(--foreground))",
           primary: {
             DEFAULT: "hsl(var(--primary))",
             foreground: "hsl(var(--primary-foreground))",
           },
           secondary: {
             DEFAULT: "hsl(var(--secondary))",
             foreground: "hsl(var(--secondary-foreground))",
           },
           destructive: {
             DEFAULT: "hsl(var(--destructive))",
             foreground: "hsl(var(--destructive-foreground))",
           },
           muted: {
             DEFAULT: "hsl(var(--muted))",
             foreground: "hsl(var(--muted-foreground))",
           },
           accent: {
             DEFAULT: "hsl(var(--accent))",
             foreground: "hsl(var(--accent-foreground))",
           },
           popover: {
             DEFAULT: "hsl(var(--popover))",
             foreground: "hsl(var(--popover-foreground))",
           },
           card: {
             DEFAULT: "hsl(var(--card))",
             foreground: "hsl(var(--card-foreground))",
           },
         },
         borderRadius: {
           lg: "var(--radius)",
           md: "calc(var(--radius) - 2px)",
           sm: "calc(var(--radius) - 4px)",
         },
       },
     },
     plugins: [],
   }
   ```

3. Create `src/index.css` with Tailwind directives:
   ```css
   @tailwind base;
   @tailwind components;
   @tailwind utilities;

   @layer base {
     :root {
       --background: 0 0% 100%;
       --foreground: 222.2 84% 4.9%;
       --card: 0 0% 100%;
       --card-foreground: 222.2 84% 4.9%;
       --popover: 0 0% 100%;
       --popover-foreground: 222.2 84% 4.9%;
       --primary: 221.2 83.2% 53.3%;
       --primary-foreground: 210 40% 98%;
       --secondary: 210 40% 96.1%;
       --secondary-foreground: 222.2 47.4% 11.2%;
       --muted: 210 40% 96.1%;
       --muted-foreground: 215.4 16.3% 46.9%;
       --accent: 210 40% 96.1%;
       --accent-foreground: 222.2 47.4% 11.2%;
       --destructive: 0 84.2% 60.2%;
       --destructive-foreground: 210 40% 98%;
       --border: 214.3 31.8% 91.4%;
       --input: 214.3 31.8% 91.4%;
       --ring: 221.2 83.2% 53.3%;
       --radius: 0.5rem;
     }

     .dark {
       --background: 222.2 84% 4.9%;
       --foreground: 210 40% 98%;
       --card: 222.2 84% 4.9%;
       --card-foreground: 210 40% 98%;
       --popover: 222.2 84% 4.9%;
       --popover-foreground: 210 40% 98%;
       --primary: 217.2 91.2% 59.8%;
       --primary-foreground: 222.2 47.4% 11.2%;
       --secondary: 217.2 32.6% 17.5%;
       --secondary-foreground: 210 40% 98%;
       --muted: 217.2 32.6% 17.5%;
       --muted-foreground: 215 20.2% 65.1%;
       --accent: 217.2 32.6% 17.5%;
       --accent-foreground: 210 40% 98%;
       --destructive: 0 62.8% 30.6%;
       --destructive-foreground: 210 40% 98%;
       --border: 217.2 32.6% 17.5%;
       --input: 217.2 32.6% 17.5%;
       --ring: 224.3 76.3% 48%;
     }
   }

   @layer base {
     * {
       @apply border-border;
     }
     body {
       @apply bg-background text-foreground;
     }
   }
   ```

### Step 6: Setup Shadcn UI

1. Initialize Shadcn UI:
   ```bash
   npx shadcn-ui@latest init
   ```

2. Follow the prompts to configure Shadcn UI with your project settings.

3. Install common Shadcn UI components:
   ```bash
   npx shadcn-ui@latest add button card dialog dropdown-menu input label select table tabs toast
   ```

### Step 7: Configure ESLint and Prettier

1. Create `.eslintrc.cjs` in the frontend directory:
   ```javascript
   module.exports = {
     root: true,
     env: { browser: true, es2020: true },
     extends: [
       'eslint:recommended',
       'plugin:@typescript-eslint/recommended',
       'plugin:react-hooks/recommended',
       'plugin:prettier/recommended',
     ],
     ignorePatterns: ['dist', '.eslintrc.cjs'],
     parser: '@typescript-eslint/parser',
     plugins: ['react-refresh'],
     rules: {
       'react-refresh/only-export-components': [
         'warn',
         { allowConstantExport: true },
       ],
     },
   }
   ```

2. Create `.prettierrc` in the frontend directory:
   ```json
   {
     "semi": true,
     "tabWidth": 2,
     "printWidth": 100,
     "singleQuote": true,
     "trailingComma": "es5",
     "jsxSingleQuote": false,
     "bracketSpacing": true
   }
   ```

3. Install ESLint and Prettier dependencies:
   ```bash
   npm install --save-dev eslint prettier eslint-config-prettier eslint-plugin-prettier
   ```

4. Add scripts to `package.json`:
   ```json
   "scripts": {
     "lint": "eslint . --ext ts,tsx --report-unused-disable-directives --max-warnings 0",
     "format": "prettier --write \"src/**/*.{ts,tsx,css}\""
   }
   ```

## Part 2: Maven Integration

### Step 1: Configure frontend-maven-plugin

Add the following to your `pom.xml` in the `<build><plugins>` section:

```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.15.1</version>
    <configuration>
        <workingDirectory>src/main/frontend</workingDirectory>
        <installDirectory>target</installDirectory>
        <nodeVersion>v22.16.0</nodeVersion>
        <npmVersion>11.4.0</npmVersion>
    </configuration>
    <executions>
        <execution>
            <id>install-npm</id>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
            <phase>generate-resources</phase>
        </execution>
        <execution>
            <id>npm-install</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <phase>generate-resources</phase>
            <configuration>
                <arguments>install</arguments>
            </configuration>
        </execution>
        <execution>
            <id>npm-build</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <phase>generate-resources</phase>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
        </execution>
        <execution>
            <id>npm-test</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <phase>test</phase>
            <configuration>
                <arguments>test</arguments>
                <environmentVariables>
                    <CI>true</CI>
                </environmentVariables>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Step 2: Configure Maven Clean Plugin

Add the following to your `pom.xml` in the `<build><plugins>` section:

```xml
<plugin>
    <artifactId>maven-clean-plugin</artifactId>
    <configuration>
        <filesets>
            <fileset>
                <directory>src/main/resources/static</directory>
            </fileset>
        </filesets>
    </configuration>
</plugin>
```

## Part 3: API Service Implementation

### Step 1: Generate TypeScript Types from OpenAPI

1. Install OpenAPI TypeScript Codegen:
   ```bash
   npm install --save-dev openapi-typescript-codegen
   ```

2. Add a script to `package.json` to generate types:
   ```json
   "scripts": {
     "generate-api": "openapi --input ../../openapi/openapi/openapi.yaml --output ./src/api --client axios"
   }
   ```

3. Run the script to generate API types:
   ```bash
   npm run generate-api
   ```

### Step 2: Create API Service Layer

Create a reusable Axios instance in `src/services/api.ts`:

```typescript
import axios from 'axios';

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || '/api/v1';

const apiClient = axios.create({
  baseURL: apiBaseUrl,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle errors globally
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default apiClient;
```

### Step 3: Create Service Modules for Each Resource

Create service modules for each resource type:

#### Apparel Service (`src/services/apparelService.ts`):

```typescript
import apiClient from './api';
import { ApparelDto, PageOfApparelDto } from '../api/models';

export const apparelService = {
  getAllApparels: async (
    apparelName?: string,
    apparelStyle?: string,
    page = 0,
    size = 20
  ): Promise<PageOfApparelDto> => {
    const params = new URLSearchParams();
    if (apparelName) params.append('apparelName', apparelName);
    if (apparelStyle) params.append('apparelStyle', apparelStyle);
    params.append('page', page.toString());
    params.append('size', size.toString());

    const response = await apiClient.get('/apparels', { params });
    return response.data;
  },

  getApparelById: async (id: number): Promise<ApparelDto> => {
    const response = await apiClient.get(`/apparels/${id}`);
    return response.data;
  },

  createApparel: async (apparel: Omit<ApparelDto, 'id' | 'version' | 'createdDate' | 'updateDate'>): Promise<ApparelDto> => {
    const response = await apiClient.post('/apparels', apparel);
    return response.data;
  },

  updateApparel: async (id: number, apparel: Omit<ApparelDto, 'id' | 'version' | 'createdDate' | 'updateDate'>): Promise<ApparelDto> => {
    const response = await apiClient.put(`/apparels/${id}`, apparel);
    return response.data;
  },

  patchApparel: async (id: number, apparel: Partial<ApparelDto>): Promise<ApparelDto> => {
    const response = await apiClient.patch(`/apparels/${id}`, apparel);
    return response.data;
  },

  deleteApparel: async (id: number): Promise<void> => {
    await apiClient.delete(`/apparels/${id}`);
  }
};
```

#### Similar service modules should be created for ApparelOrder and Customer resources.

## Part 4: React Components and Routing

### Step 1: Setup React Router

1. Create a router configuration in `src/router.tsx`:

```typescript
import { createBrowserRouter } from 'react-router-dom';
import App from './App';
import ApparelList from './pages/apparel/ApparelList';
import ApparelDetail from './pages/apparel/ApparelDetail';
import ApparelCreate from './pages/apparel/ApparelCreate';
import ApparelEdit from './pages/apparel/ApparelEdit';
import CustomerList from './pages/customer/CustomerList';
import CustomerDetail from './pages/customer/CustomerDetail';
import CustomerCreate from './pages/customer/CustomerCreate';
import CustomerEdit from './pages/customer/CustomerEdit';
import ApparelOrderList from './pages/order/ApparelOrderList';
import ApparelOrderDetail from './pages/order/ApparelOrderDetail';
import ApparelOrderCreate from './pages/order/ApparelOrderCreate';
import ApparelOrderEdit from './pages/order/ApparelOrderEdit';
import NotFound from './pages/NotFound';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    errorElement: <NotFound />,
    children: [
      { index: true, element: <ApparelList /> },
      { path: 'apparels', element: <ApparelList /> },
      { path: 'apparels/new', element: <ApparelCreate /> },
      { path: 'apparels/:id', element: <ApparelDetail /> },
      { path: 'apparels/:id/edit', element: <ApparelEdit /> },
      { path: 'customers', element: <CustomerList /> },
      { path: 'customers/new', element: <CustomerCreate /> },
      { path: 'customers/:id', element: <CustomerDetail /> },
      { path: 'customers/:id/edit', element: <CustomerEdit /> },
      { path: 'apparel-orders', element: <ApparelOrderList /> },
      { path: 'apparel-orders/new', element: <ApparelOrderCreate /> },
      { path: 'apparel-orders/:id', element: <ApparelOrderDetail /> },
      { path: 'apparel-orders/:id/edit', element: <ApparelOrderEdit /> },
    ],
  },
]);
```

2. Update `src/main.tsx` to use the router:

```typescript
import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { router } from './router';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
```

### Step 2: Create Layout Components

Create a layout component in `src/components/layout/Layout.tsx`:

```typescript
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import { Toaster } from '../ui/toaster';

export default function Layout() {
  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container mx-auto py-6 px-4">
        <Outlet />
      </main>
      <Toaster />
    </div>
  );
}
```

Create a navigation component in `src/components/layout/Navbar.tsx`:

```typescript
import { Link } from 'react-router-dom';
import { Apparel, Users, ShoppingCart } from 'lucide-react';

export default function Navbar() {
  return (
    <nav className="bg-primary text-primary-foreground">
      <div className="container mx-auto px-4">
        <div className="flex h-16 items-center justify-between">
          <div className="flex items-center">
            <Link to="/" className="text-xl font-bold">
              Apparel Service
            </Link>
          </div>
          <div className="flex items-center space-x-6">
            <Link to="/apparels" className="flex items-center space-x-2 hover:text-primary-foreground/80">
              <Apparel size={20} />
              <span>Apparels</span>
            </Link>
            <Link to="/customers" className="flex items-center space-x-2 hover:text-primary-foreground/80">
              <Users size={20} />
              <span>Customers</span>
            </Link>
            <Link to="/apparel-orders" className="flex items-center space-x-2 hover:text-primary-foreground/80">
              <ShoppingCart size={20} />
              <span>Orders</span>
            </Link>
          </div>
        </div>
      </div>
    </nav>
  );
}
```

Update `src/App.tsx` to use the layout:

```typescript
import Layout from './components/layout/Layout';

function App() {
  return <Layout />;
}

export default App;
```

### Step 3: Create Custom Hooks for State Management

Create a custom hook for apparel data in `src/hooks/useApparel.ts`:

```typescript
import { useState, useEffect, useCallback } from 'react';
import { apparelService } from '../services/apparelService';
import { ApparelDto, PageOfApparelDto } from '../api/models';

export function useApparels(initialPage = 0, initialSize = 20) {
  const [apparels, setApparels] = useState<PageOfApparelDto | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);
  const [page, setPage] = useState(initialPage);
  const [size, setSize] = useState(initialSize);
  const [filters, setFilters] = useState<{ apparelName?: string; apparelStyle?: string }>({});

  const fetchApparels = useCallback(async () => {
    try {
      setLoading(true);
      const data = await apparelService.getAllApparels(
        filters.apparelName,
        filters.apparelStyle,
        page,
        size
      );
      setApparels(data);
      setError(null);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An error occurred'));
    } finally {
      setLoading(false);
    }
  }, [page, size, filters]);

  useEffect(() => {
    fetchApparels();
  }, [fetchApparels]);

  const updateFilters = useCallback((newFilters: { apparelName?: string; apparelStyle?: string }) => {
    setFilters(newFilters);
    setPage(0); // Reset to first page when filters change
  }, []);

  return {
    apparels,
    loading,
    error,
    page,
    size,
    filters,
    setPage,
    setSize,
    updateFilters,
    refetch: fetchApparels,
  };
}

export function useApparel(id: number | null) {
  const [apparel, setApparel] = useState<ApparelDto | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  const fetchApparel = useCallback(async () => {
    if (id === null) return;

    try {
      setLoading(true);
      const data = await apparelService.getApparelById(id);
      setApparel(data);
      setError(null);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An error occurred'));
      setApparel(null);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    fetchApparel();
  }, [fetchApparel]);

  const updateApparel = useCallback(async (updatedApparel: Omit<ApparelDto, 'id' | 'version' | 'createdDate' | 'updateDate'>) => {
    if (id === null) return null;

    try {
      setLoading(true);
      const data = await apparelService.updateApparel(id, updatedApparel);
      setApparel(data);
      setError(null);
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [id]);

  const deleteApparel = useCallback(async () => {
    if (id === null) return false;

    try {
      setLoading(true);
      await apparelService.deleteApparel(id);
      setApparel(null);
      setError(null);
      return true;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An error occurred'));
      return false;
    } finally {
      setLoading(false);
    }
  }, [id]);

  return {
    apparel,
    loading,
    error,
    refetch: fetchApparel,
    updateApparel,
    deleteApparel,
  };
}
```

Create similar hooks for ApparelOrder and Customer resources.

## Part 5: Implementing CRUD Pages

### Step 1: Create Apparel List Page

Create `src/pages/apparel/ApparelList.tsx`:

```typescript
import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useApparels } from '../../hooks/useApparel';
import { Button } from '../../components/ui/button';
import { Input } from '../../components/ui/input';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../../components/ui/table';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/card';
import { Edit, Trash, Plus, Search, RefreshCw } from 'lucide-react';

export default function ApparelList() {
  const [nameFilter, setNameFilter] = useState('');
  const [styleFilter, setStyleFilter] = useState('');
  const { apparels, loading, error, page, setPage, updateFilters, refetch } = useApparels();

  const handleSearch = () => {
    updateFilters({
      apparelName: nameFilter || undefined,
      apparelStyle: styleFilter || undefined,
    });
  };

  const handleClearFilters = () => {
    setNameFilter('');
    setStyleFilter('');
    updateFilters({});
  };

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between">
        <CardTitle>Apparel Inventory</CardTitle>
        <Link to="/apparels/new">
          <Button>
            <Plus className="mr-2 h-4 w-4" />
            Add Apparel
          </Button>
        </Link>
      </CardHeader>
      <CardContent>
        <div className="mb-4 flex flex-col gap-4 sm:flex-row">
          <div className="flex flex-1 items-center gap-2">
            <Input
              placeholder="Filter by name"
              value={nameFilter}
              onChange={(e) => setNameFilter(e.target.value)}
              className="max-w-xs"
            />
            <Input
              placeholder="Filter by style"
              value={styleFilter}
              onChange={(e) => setStyleFilter(e.target.value)}
              className="max-w-xs"
            />
            <Button variant="outline" onClick={handleSearch}>
              <Search className="mr-2 h-4 w-4" />
              Search
            </Button>
            <Button variant="outline" onClick={handleClearFilters}>
              Clear
            </Button>
          </div>
          <Button variant="outline" onClick={refetch} disabled={loading}>
            <RefreshCw className={`mr-2 h-4 w-4 ${loading ? 'animate-spin' : ''}`} />
            Refresh
          </Button>
        </div>

        {error && (
          <div className="mb-4 rounded-md bg-destructive/15 p-3 text-destructive">
            Error loading apparels: {error.message}
          </div>
        )}

        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Name</TableHead>
              <TableHead>Style</TableHead>
              <TableHead>Price</TableHead>
              <TableHead>Quantity</TableHead>
              <TableHead>UPC</TableHead>
              <TableHead className="text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell colSpan={6} className="text-center">
                  Loading...
                </TableCell>
              </TableRow>
            ) : apparels?.content?.length ? (
              apparels.content.map((apparel) => (
                <TableRow key={apparel.id}>
                  <TableCell>{apparel.apparelName}</TableCell>
                  <TableCell>{apparel.apparelStyle}</TableCell>
                  <TableCell>${apparel.price?.toFixed(2)}</TableCell>
                  <TableCell>{apparel.quantityOnHand}</TableCell>
                  <TableCell>{apparel.upc}</TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Link to={`/apparels/${apparel.id}`}>
                        <Button variant="outline" size="icon">
                          <Search className="h-4 w-4" />
                        </Button>
                      </Link>
                      <Link to={`/apparels/${apparel.id}/edit`}>
                        <Button variant="outline" size="icon">
                          <Edit className="h-4 w-4" />
                        </Button>
                      </Link>
                    </div>
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={6} className="text-center">
                  No apparels found
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>

        {apparels && apparels.totalPages > 1 && (
          <div className="mt-4 flex items-center justify-between">
            <div>
              Showing page {apparels.number + 1} of {apparels.totalPages}
            </div>
            <div className="flex gap-2">
              <Button
                variant="outline"
                onClick={() => setPage(Math.max(0, page - 1))}
                disabled={apparels.first}
              >
                Previous
              </Button>
              <Button
                variant="outline"
                onClick={() => setPage(page + 1)}
                disabled={apparels.last}
              >
                Next
              </Button>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
```

### Step 2: Create Apparel Detail Page

Create `src/pages/apparel/ApparelDetail.tsx`:

```typescript
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useApparel } from '../../hooks/useApparel';
import { Button } from '../../components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/ui/card';
import { Edit, ArrowLeft, Trash } from 'lucide-react';
import { useState } from 'react';
import { useToast } from '../../components/ui/use-toast';

export default function ApparelDetail() {
  const { id } = useParams<{ id: string }>();
  const apparelId = id ? parseInt(id, 10) : null;
  const { apparel, loading, error, deleteApparel } = useApparel(apparelId);
  const [isDeleting, setIsDeleting] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete this apparel?')) {
      return;
    }

    setIsDeleting(true);
    try {
      const success = await deleteApparel();
      if (success) {
        toast({
          title: 'Apparel deleted',
          description: 'The apparel has been successfully deleted.',
        });
        navigate('/apparels');
      } else {
        toast({
          title: 'Error',
          description: 'Failed to delete the apparel.',
          variant: 'destructive',
        });
      }
    } catch (err) {
      toast({
        title: 'Error',
        description: 'An unexpected error occurred.',
        variant: 'destructive',
      });
    } finally {
      setIsDeleting(false);
    }
  };

  if (loading) {
    return <div className="text-center py-8">Loading...</div>;
  }

  if (error) {
    return (
      <div className="rounded-md bg-destructive/15 p-4 text-destructive">
        Error: {error.message}
      </div>
    );
  }

  if (!apparel) {
    return <div className="text-center py-8">Apparel not found</div>;
  }

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <Button variant="outline" onClick={() => navigate('/apparels')}>
          <ArrowLeft className="mr-2 h-4 w-4" />
          Back to List
        </Button>
        <div className="flex gap-2">
          <Link to={`/apparels/${apparel.id}/edit`}>
            <Button>
              <Edit className="mr-2 h-4 w-4" />
              Edit
            </Button>
          </Link>
          <Button variant="destructive" onClick={handleDelete} disabled={isDeleting}>
            <Trash className="mr-2 h-4 w-4" />
            {isDeleting ? 'Deleting...' : 'Delete'}
          </Button>
        </div>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>{apparel.apparelName}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div>
              <h3 className="text-lg font-semibold">Details</h3>
              <div className="mt-2 space-y-2">
                <div className="flex justify-between border-b pb-2">
                  <span className="font-medium">Style:</span>
                  <span>{apparel.apparelStyle}</span>
                </div>
                <div className="flex justify-between border-b pb-2">
                  <span className="font-medium">Price:</span>
                  <span>${apparel.price?.toFixed(2)}</span>
                </div>
                <div className="flex justify-between border-b pb-2">
                  <span className="font-medium">Quantity on Hand:</span>
                  <span>{apparel.quantityOnHand}</span>
                </div>
                <div className="flex justify-between border-b pb-2">
                  <span className="font-medium">UPC:</span>
                  <span>{apparel.upc}</span>
                </div>
              </div>
            </div>
            <div>
              <h3 className="text-lg font-semibold">System Information</h3>
              <div className="mt-2 space-y-2">
                <div className="flex justify-between border-b pb-2">
                  <span className="font-medium">ID:</span>
                  <span>{apparel.id}</span>
                </div>
                <div className="flex justify-between border-b pb-2">
                  <span className="font-medium">Version:</span>
                  <span>{apparel.version}</span>
                </div>
                <div className="flex justify-between border-b pb-2">
                  <span className="font-medium">Created Date:</span>
                  <span>{new Date(apparel.createdDate!).toLocaleString()}</span>
                </div>
                <div className="flex justify-between border-b pb-2">
                  <span className="font-medium">Last Updated:</span>
                  <span>{new Date(apparel.updateDate!).toLocaleString()}</span>
                </div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
```

### Step 3: Create Apparel Form Component

Create a reusable form component in `src/components/apparel/ApparelForm.tsx`:

```typescript
import { useState } from 'react';
import { ApparelDto } from '../../api/models';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { Label } from '../ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../ui/select';
import { Card, CardContent } from '../ui/card';

type ApparelFormProps = {
  initialData?: Partial<ApparelDto>;
  onSubmit: (data: Omit<ApparelDto, 'id' | 'version' | 'createdDate' | 'updateDate'>) => void;
  isSubmitting: boolean;
};

const APPAREL_STYLES = [
  'Loose',
  'Fit',
  'Oversize',
  'Stretch',
];

export default function ApparelForm({ initialData, onSubmit, isSubmitting }: ApparelFormProps) {
  const [formData, setFormData] = useState({
    apparelName: initialData?.apparelName || '',
    apparelStyle: initialData?.apparelStyle || '',
    upc: initialData?.upc || '',
    price: initialData?.price?.toString() || '',
    quantityOnHand: initialData?.quantityOnHand?.toString() || '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));

    // Clear error when field is edited
    if (errors[name]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  const handleStyleChange = (value: string) => {
    setFormData((prev) => ({ ...prev, apparelStyle: value }));

    // Clear error when field is edited
    if (errors.apparelStyle) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors.apparelStyle;
        return newErrors;
      });
    }
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.apparelName.trim()) {
      newErrors.apparelName = 'Apparel name is required';
    }

    if (!formData.apparelStyle) {
      newErrors.apparelStyle = 'Apparel style is required';
    }

    if (!formData.upc.trim()) {
      newErrors.upc = 'UPC is required';
    }

    if (!formData.price.trim()) {
      newErrors.price = 'Price is required';
    } else if (isNaN(parseFloat(formData.price)) || parseFloat(formData.price) <= 0) {
      newErrors.price = 'Price must be a positive number';
    }

    if (!formData.quantityOnHand.trim()) {
      newErrors.quantityOnHand = 'Quantity is required';
    } else if (isNaN(parseInt(formData.quantityOnHand)) || parseInt(formData.quantityOnHand) < 0) {
      newErrors.quantityOnHand = 'Quantity must be a non-negative integer';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    onSubmit({
      apparelName: formData.apparelName,
      apparelStyle: formData.apparelStyle,
      upc: formData.upc,
      price: parseFloat(formData.price),
      quantityOnHand: parseInt(formData.quantityOnHand),
    });
  };

  return (
    <Card>
      <CardContent className="pt-6">
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-4">
            <div>
              <Label htmlFor="apparelName">Apparel Name</Label>
              <Input
                id="apparelName"
                name="apparelName"
                value={formData.apparelName}
                onChange={handleChange}
                className={errors.apparelName ? 'border-destructive' : ''}
              />
              {errors.apparelName && (
                <p className="mt-1 text-sm text-destructive">{errors.apparelName}</p>
              )}
            </div>

            <div>
              <Label htmlFor="apparelStyle">Apparel Style</Label>
              <Select value={formData.apparelStyle} onValueChange={handleStyleChange}>
                <SelectTrigger id="apparelStyle" className={errors.apparelStyle ? 'border-destructive' : ''}>
                  <SelectValue placeholder="Select a apparel style" />
                </SelectTrigger>
                <SelectContent>
                  {BEER_STYLES.map((style) => (
                    <SelectItem key={style} value={style}>
                      {style.replace('_', ' ')}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {errors.apparelStyle && (
                <p className="mt-1 text-sm text-destructive">{errors.apparelStyle}</p>
              )}
            </div>

            <div>
              <Label htmlFor="upc">UPC</Label>
              <Input
                id="upc"
                name="upc"
                value={formData.upc}
                onChange={handleChange}
                className={errors.upc ? 'border-destructive' : ''}
              />
              {errors.upc && <p className="mt-1 text-sm text-destructive">{errors.upc}</p>}
            </div>

            <div>
              <Label htmlFor="price">Price</Label>
              <Input
                id="price"
                name="price"
                type="number"
                step="0.01"
                min="0"
                value={formData.price}
                onChange={handleChange}
                className={errors.price ? 'border-destructive' : ''}
              />
              {errors.price && <p className="mt-1 text-sm text-destructive">{errors.price}</p>}
            </div>

            <div>
              <Label htmlFor="quantityOnHand">Quantity on Hand</Label>
              <Input
                id="quantityOnHand"
                name="quantityOnHand"
                type="number"
                min="0"
                value={formData.quantityOnHand}
                onChange={handleChange}
                className={errors.quantityOnHand ? 'border-destructive' : ''}
              />
              {errors.quantityOnHand && (
                <p className="mt-1 text-sm text-destructive">{errors.quantityOnHand}</p>
              )}
            </div>
          </div>

          <div className="flex justify-end gap-2">
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Saving...' : 'Save Apparel'}
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}
```

### Step 4: Create Apparel Create and Edit Pages

Create `src/pages/apparel/ApparelCreate.tsx`:

```typescript
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { apparelService } from '../../services/apparelService';
import ApparelForm from '../../components/apparel/ApparelForm';
import { Button } from '../../components/ui/button';
import { ArrowLeft } from 'lucide-react';
import { useToast } from '../../components/ui/use-toast';

export default function ApparelCreate() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleSubmit = async (data: any) => {
    setIsSubmitting(true);
    try {
      const newApparel = await apparelService.createApparel(data);
      toast({
        title: 'Apparel created',
        description: 'The apparel has been successfully created.',
      });
      navigate(`/apparels/${newApparel.id}`);
    } catch (err) {
      toast({
        title: 'Error',
        description: 'Failed to create the apparel.',
        variant: 'destructive',
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <Button variant="outline" onClick={() => navigate('/apparels')}>
          <ArrowLeft className="mr-2 h-4 w-4" />
          Back to List
        </Button>
        <h1 className="text-2xl font-bold">Create New Apparel</h1>
      </div>
      <ApparelForm onSubmit={handleSubmit} isSubmitting={isSubmitting} />
    </div>
  );
}
```

Create `src/pages/apparel/ApparelEdit.tsx`:

```typescript
import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useApparel } from '../../hooks/useApparel';
import ApparelForm from '../../components/apparel/ApparelForm';
import { Button } from '../../components/ui/button';
import { ArrowLeft } from 'lucide-react';
import { useToast } from '../../components/ui/use-toast';

export default function ApparelEdit() {
  const { id } = useParams<{ id: string }>();
  const apparelId = id ? parseInt(id, 10) : null;
  const { apparel, loading, error, updateApparel } = useApparel(apparelId);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleSubmit = async (data: any) => {
    setIsSubmitting(true);
    try {
      const updatedApparel = await updateApparel(data);
      if (updatedApparel) {
        toast({
          title: 'Apparel updated',
          description: 'The apparel has been successfully updated.',
        });
        navigate(`/apparels/${updatedApparel.id}`);
      } else {
        toast({
          title: 'Error',
          description: 'Failed to update the apparel.',
          variant: 'destructive',
        });
      }
    } catch (err) {
      toast({
        title: 'Error',
        description: 'An unexpected error occurred.',
        variant: 'destructive',
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return <div className="text-center py-8">Loading...</div>;
  }

  if (error) {
    return (
      <div className="rounded-md bg-destructive/15 p-4 text-destructive">
        Error: {error.message}
      </div>
    );
  }

  if (!apparel) {
    return <div className="text-center py-8">Apparel not found</div>;
  }

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <Button variant="outline" onClick={() => navigate(`/apparels/${apparel.id}`)}>
          <ArrowLeft className="mr-2 h-4 w-4" />
          Back to Details
        </Button>
        <h1 className="text-2xl font-bold">Edit Apparel</h1>
      </div>
      <ApparelForm initialData={apparel} onSubmit={handleSubmit} isSubmitting={isSubmitting} />
    </div>
  );
}
```

## Part 6: Testing

### Step 1: Configure Jest

1. Update `package.json` to configure Jest:

```json
"scripts": {
  "test": "jest",
  "test:watch": "jest --watch"
},
"jest": {
  "preset": "ts-jest",
  "testEnvironment": "jsdom",
  "setupFilesAfterEnv": ["<rootDir>/src/setupTests.ts"],
  "moduleNameMapper": {
    "^@/(.*)$": "<rootDir>/src/$1"
  }
}
```

2. Create `src/setupTests.ts`:

```typescript
import '@testing-library/jest-dom';
```

### Step 2: Write Tests for Components

Create a test for the ApparelForm component in `src/components/apparel/__tests__/ApparelForm.test.tsx`:

```typescript
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ApparelForm from '../ApparelForm';

describe('ApparelForm', () => {
  const mockOnSubmit = jest.fn();

  beforeEach(() => {
    mockOnSubmit.mockClear();
  });

  it('renders the form with empty fields when no initial data is provided', () => {
    render(<ApparelForm onSubmit={mockOnSubmit} isSubmitting={false} />);

    expect(screen.getByLabelText(/apparel name/i)).toHaveValue('');
    expect(screen.getByText(/select a apparel style/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/upc/i)).toHaveValue('');
    expect(screen.getByLabelText(/price/i)).toHaveValue('');
    expect(screen.getByLabelText(/quantity on hand/i)).toHaveValue('');
  });

  it('renders the form with initial data when provided', () => {
    const initialData = {
      apparelName: 'Test Apparel',
      apparelStyle: 'Loose',
      upc: '123456789',
      price: 9.99,
      quantityOnHand: 100,
    };

    render(<ApparelForm initialData={initialData} onSubmit={mockOnSubmit} isSubmitting={false} />);

    expect(screen.getByLabelText(/apparel name/i)).toHaveValue('Test Apparel');
    expect(screen.getByLabelText(/upc/i)).toHaveValue('123456789');
    expect(screen.getByLabelText(/price/i)).toHaveValue('9.99');
    expect(screen.getByLabelText(/quantity on hand/i)).toHaveValue('100');
  });

  it('shows validation errors when submitting with empty fields', async () => {
    render(<ApparelForm onSubmit={mockOnSubmit} isSubmitting={false} />);

    fireEvent.click(screen.getByText(/save apparel/i));

    await waitFor(() => {
      expect(screen.getByText(/apparel name is required/i)).toBeInTheDocument();
      expect(screen.getByText(/apparel style is required/i)).toBeInTheDocument();
      expect(screen.getByText(/upc is required/i)).toBeInTheDocument();
      expect(screen.getByText(/price is required/i)).toBeInTheDocument();
      expect(screen.getByText(/quantity is required/i)).toBeInTheDocument();
    });

    expect(mockOnSubmit).not.toHaveBeenCalled();
  });

  it('submits the form with valid data', async () => {
    render(<ApparelForm onSubmit={mockOnSubmit} isSubmitting={false} />);

    fireEvent.change(screen.getByLabelText(/apparel name/i), { target: { value: 'Test Apparel' } });

    // Select a apparel style
    fireEvent.click(screen.getByText(/select a apparel style/i));
    fireEvent.click(screen.getByText(/ipa/i));

    fireEvent.change(screen.getByLabelText(/upc/i), { target: { value: '123456789' } });
    fireEvent.change(screen.getByLabelText(/price/i), { target: { value: '9.99' } });
    fireEvent.change(screen.getByLabelText(/quantity on hand/i), { target: { value: '100' } });

    fireEvent.click(screen.getByText(/save apparel/i));

    await waitFor(() => {
      expect(mockOnSubmit).toHaveBeenCalledWith({
        apparelName: 'Test Apparel',
        apparelStyle: 'Loose',
        upc: '123456789',
        price: 9.99,
        quantityOnHand: 100,
      });
    });
  });

  it('disables the submit button when isSubmitting is true', () => {
    render(<ApparelForm onSubmit={mockOnSubmit} isSubmitting={true} />);

    expect(screen.getByText(/saving/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /saving/i })).toBeDisabled();
  });
});
```

### Step 3: Write Tests for API Services

Create a test for the Apparel Service in `src/services/__tests__/apparelService.test.ts`:

```typescript
import { apparelService } from '../apparelService';
import apiClient from '../api';

jest.mock('../api');
const mockedApiClient = apiClient as jest.Mocked<typeof apiClient>;

describe('apparelService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getAllApparels', () => {
    it('should fetch apparels with default pagination', async () => {
      const mockResponse = {
        data: {
          content: [
            { id: 1, apparelName: 'Test Apparel 1' },
            { id: 2, apparelName: 'Test Apparel 2' },
          ],
          totalElements: 2,
          totalPages: 1,
          size: 20,
          number: 0,
          first: true,
          last: true,
        },
      };

      mockedApiClient.get.mockResolvedValueOnce(mockResponse);

      const result = await apparelService.getAllApparels();

      expect(mockedApiClient.get).toHaveBeenCalledWith('/apparels', {
        params: expect.any(URLSearchParams),
      });

      const params = new URLSearchParams();
      params.append('page', '0');
      params.append('size', '20');

      expect(mockedApiClient.get.mock.calls[0][1].params.toString()).toBe(params.toString());
      expect(result).toEqual(mockResponse.data);
    });

    it('should fetch apparels with filters and pagination', async () => {
      const mockResponse = {
        data: {
          content: [{ id: 1, apparelName: 'Loose Apparel' }],
          totalElements: 1,
          totalPages: 1,
          size: 10,
          number: 2,
          first: false,
          last: true,
        },
      };

      mockedApiClient.get.mockResolvedValueOnce(mockResponse);

      const result = await apparelService.getAllApparels('Loose', 'Loose', 2, 10);

      expect(mockedApiClient.get).toHaveBeenCalledWith('/apparels', {
        params: expect.any(URLSearchParams),
      });

      const params = new URLSearchParams();
      params.append('apparelName', 'Loose');
      params.append('apparelStyle', 'Loose');
      params.append('page', '2');
      params.append('size', '10');

      expect(mockedApiClient.get.mock.calls[0][1].params.toString()).toBe(params.toString());
      expect(result).toEqual(mockResponse.data);
    });
  });

  describe('getApparelById', () => {
    it('should fetch a apparel by id', async () => {
      const mockResponse = {
        data: { id: 1, apparelName: 'Test Apparel' },
      };

      mockedApiClient.get.mockResolvedValueOnce(mockResponse);

      const result = await apparelService.getApparelById(1);

      expect(mockedApiClient.get).toHaveBeenCalledWith('/apparels/1');
      expect(result).toEqual(mockResponse.data);
    });
  });

  describe('createApparel', () => {
    it('should create a new apparel', async () => {
      const newApparel = {
        apparelName: 'New Apparel',
        apparelStyle: 'Loose',
        upc: '123456789',
        price: 9.99,
        quantityOnHand: 100,
      };

      const mockResponse = {
        data: { id: 1, ...newApparel },
      };

      mockedApiClient.post.mockResolvedValueOnce(mockResponse);

      const result = await apparelService.createApparel(newApparel);

      expect(mockedApiClient.post).toHaveBeenCalledWith('/apparels', newApparel);
      expect(result).toEqual(mockResponse.data);
    });
  });
});
```

## Part 7: Running and Deployment

### Step 1: Development Workflow

To run the application in development mode:

1. Start the Spring Boot backend:
   ```bash
   ./mvnw spring-boot:run
   ```

2. In a separate terminal, start the Vite development server:
   ```bash
   cd src/main/frontend
   npm run dev
   ```

3. Access the application:
   - Backend API: http://localhost:8080/api/v1
   - Frontend UI: http://localhost:3000

The Vite development server will proxy API requests to the Spring Boot backend, so you can work on both simultaneously with hot reloading.

### Step 2: Production Build

To build the application for production:

1. Run the Maven build:
   ```bash
   ./mvnw clean package
   ```

This will:
- Install Node.js and npm if not already installed
- Install frontend dependencies
- Build the React application
- Copy the built frontend assets to `src/main/resources/static`
- Package everything into a single executable JAR file

2. Run the packaged application:
   ```bash
   java -jar target/juniemvc-0.0.1-SNAPSHOT.jar
   ```

3. Access the application at http://localhost:8080

### Step 3: Continuous Integration

For CI/CD pipelines, you can use the same Maven command to build the application:

```bash
./mvnw clean package
```

This ensures that both the backend and frontend are built and tested in a single step.

## Part 8: Project Guidelines Update

Update the `.junie/guidelines.md` file with the following information:

```markdown
# Project Guidelines

## Project Structure

The project now includes a React frontend in addition to the Spring Boot backend:

```
juniemvc/
├── src/
│   ├── main/
│   │   ├── frontend/           # React frontend code
│   │   │   ├── public/         # Static assets
│   │   │   ├── src/            # React source code
│   │   │   ├── package.json    # Frontend dependencies
│   │   │   └── vite.config.ts  # Vite configuration
│   │   ├── java/               # Java backend code
│   │   └── resources/
│   │       └── static/         # Built frontend assets (generated)
│   └── test/                   # Backend tests
├── openapi/                    # OpenAPI documentation
├── pom.xml                     # Maven configuration
└── .junie/                     # Project guidelines
```

## Build Commands

### Development

1. Start the Spring Boot backend:
   ```bash
   ./mvnw spring-boot:run
   ```

2. Start the React development server:
   ```bash
   cd src/main/frontend
   npm run dev
   ```

### Production Build

Build the complete application (backend + frontend):
```bash
./mvnw clean package
```

Run the packaged application:
```bash
java -jar target/juniemvc-0.0.1-SNAPSHOT.jar
```

## Testing

### Backend Tests

Run backend tests:
```bash
./mvnw test
```

### Frontend Tests

Run frontend tests:
```bash
cd src/main/frontend
npm test
```

## API Documentation

The API documentation is available at:
- Development: http://localhost:8080/swagger-ui.html
- Production: https://your-production-url/swagger-ui.html
```

## Conclusion

This guide has provided detailed instructions for implementing a React frontend for the Spring Boot Apparel Service application. By following these steps, you've created a modern, type-safe, and well-tested frontend that integrates seamlessly with the existing backend.

The implementation includes:
- A complete React application with TypeScript
- Integration with the Spring Boot backend API
- CRUD operations for apparels, customers, and apparel orders
- A responsive UI using Shadcn UI and Tailwind CSS
- Comprehensive testing with Jest and React Testing Library
- Maven integration for a unified build process

The resulting application provides a user-friendly interface for managing apparel inventory, customers, and orders, while maintaining the robustness and reliability of the Spring Boot backend.
