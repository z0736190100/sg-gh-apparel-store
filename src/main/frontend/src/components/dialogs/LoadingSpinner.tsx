import { Loader2 } from 'lucide-react';

interface LoadingSpinnerProps {
  size?: 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
  message?: string;
  centered?: boolean;
}

/**
 * Simple loading spinner component
 * Can be used inline or centered
 */
const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({
  size = 'md',
  className = '',
  message,
  centered = false,
}) => {
  const getSizeClasses = () => {
    switch (size) {
      case 'sm':
        return 'h-4 w-4';
      case 'lg':
        return 'h-8 w-8';
      case 'xl':
        return 'h-12 w-12';
      default:
        return 'h-6 w-6';
    }
  };

  const spinner = (
    <div className={`flex items-center gap-2 ${className}`}>
      <Loader2 className={`${getSizeClasses()} animate-spin text-primary`} />
      {message && <span className="text-sm text-gray-600 font-medium">{message}</span>}
    </div>
  );

  if (centered) {
    return <div className="flex items-center justify-center p-4">{spinner}</div>;
  }

  return spinner;
};

export default LoadingSpinner;
