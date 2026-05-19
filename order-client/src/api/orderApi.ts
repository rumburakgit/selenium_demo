import axios from 'axios';
import type { OrderRequest, OrderResponse, Product, ValidationErrors, UnavailableError } from '../types';

const http = axios.create({ baseURL: '/api' });

export async function getProducts(): Promise<Product[]> {
  const { data } = await http.get<Product[]>('/products');
  return data;
}

export async function submitOrder(order: OrderRequest): Promise<OrderResponse> {
  try {
    const { data } = await http.post<OrderResponse>('/orders', order);
    return data;
  } catch (err) {
    if (axios.isAxiosError(err) && err.response) {
      const { status } = err.response;
      if (status === 400) throw err.response.data as ValidationErrors;
      if (status === 422) throw err.response.data as UnavailableError;
    }
    throw err;
  }
}
