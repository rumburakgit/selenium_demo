import type { OrderResponse } from '../types';

const fmt = new Intl.NumberFormat('pl-PL', { style: 'currency', currency: 'PLN' });

interface Props {
  response: OrderResponse;
  onReset: () => void;
}

export default function ConfirmationView({ response, onReset }: Props) {
  return (
    <div data-testid="confirmation-view" className="confirmation">
      <h2>Zamówienie złożone</h2>
      <dl>
        <dt>ID zamówienia</dt>
        <dd data-testid="order-id">{response.orderId}</dd>

        <dt>Status</dt>
        <dd data-testid="order-status">{response.status}</dd>

        <dt>Cena</dt>
        <dd data-testid="order-price">{fmt.format(response.totalPrice)}</dd>

        <dt>Dostawa</dt>
        <dd data-testid="order-delivery">{response.estimatedDelivery}</dd>
      </dl>
      <button data-testid="new-order-button" onClick={onReset}>
        Złóż nowe zamówienie
      </button>
    </div>
  );
}
