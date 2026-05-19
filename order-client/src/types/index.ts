export interface Product {
  id: string;
  name: string;
  price: number;
  available: boolean;
}

export interface Address {
  street: string;
  city: string;
  postalCode: string;
}

export interface OrderRequest {
  productId: string;
  quantity: number;
  address: Address;
  paymentMethod: 'CARD' | 'BLIK' | 'TRANSFER';
}

export interface OrderResponse {
  orderId: string;
  status: string;
  message: string;
  totalPrice: number;
  estimatedDelivery: string;
}

export interface ValidationErrors {
  status: number;
  errors: Record<string, string>;
}

export interface UnavailableError {
  status: number;
  message: string;
}
