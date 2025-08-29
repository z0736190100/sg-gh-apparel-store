import { useContext } from 'react';
import { ToastContext, type ToastContextType } from '../contexts/toast/ToastContext';

export const useToast = (): ToastContextType => {
  const context = useContext(ToastContext);
  if (context === undefined) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
};
