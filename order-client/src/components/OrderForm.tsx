import { useEffect, useState } from 'react';
import { getProducts, submitOrder } from '../api/orderApi';
import type { OrderResponse, Product, ValidationErrors, UnavailableError } from '../types';
import ErrorSummary from './ErrorSummary';

const fmt = new Intl.NumberFormat('pl-PL', { minimumFractionDigits: 2 });

interface Props {
  onSuccess: (response: OrderResponse) => void;
}

interface FormState {
  productId: string;
  quantity: number;
  street: string;
  city: string;
  postalCode: string;
  paymentMethod: 'CARD' | 'BLIK' | 'TRANSFER';
}

const initialForm: FormState = {
  productId: '',
  quantity: 1,
  street: '',
  city: '',
  postalCode: '',
  paymentMethod: 'CARD',
};

export default function OrderForm({ onSuccess }: Props) {
  const [products, setProducts] = useState<Product[]>([]);
  const [form, setForm] = useState<FormState>(initialForm);
  const [errors, setErrors] = useState<Record<string, string> | null>(null);
  const [unavailableMsg, setUnavailableMsg] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    getProducts().then((all) => setProducts(all.filter((p) => p.available)));
  }, []);

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === 'quantity' ? Number(value) : value,
    }));
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErrors(null);
    setUnavailableMsg(null);
    setSubmitting(true);

    try {
      const response = await submitOrder({
        productId: form.productId,
        quantity: form.quantity,
        address: {
          street: form.street,
          city: form.city,
          postalCode: form.postalCode,
        },
        paymentMethod: form.paymentMethod,
      });
      onSuccess(response);
    } catch (err) {
      const e400 = err as ValidationErrors;
      const e422 = err as UnavailableError;
      if (e400.errors) {
        setErrors(e400.errors);
      } else if (e422.message) {
        setUnavailableMsg(e422.message);
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className="order-form" onSubmit={handleSubmit} noValidate>
      <h1>Złóż zamówienie</h1>

      <ErrorSummary errors={errors} unavailableMsg={unavailableMsg} />

      <div className="field">
        <label htmlFor="productId">Produkt</label>
        <select
          id="productId"
          name="productId"
          data-testid="product-select"
          value={form.productId}
          onChange={handleChange}
        >
          <option value="">— wybierz produkt —</option>
          {products.map((p) => (
            <option key={p.id} value={p.id}>
              {p.name} — {fmt.format(p.price)} zł
            </option>
          ))}
        </select>
      </div>

      <div className="field">
        <label htmlFor="quantity">Ilość</label>
        <input
          id="quantity"
          name="quantity"
          type="number"
          min={1}
          max={99}
          data-testid="quantity-input"
          value={form.quantity}
          onChange={handleChange}
        />
      </div>

      <div className="field">
        <label htmlFor="street">Ulica</label>
        <input
          id="street"
          name="street"
          type="text"
          data-testid="street-input"
          value={form.street}
          onChange={handleChange}
        />
      </div>

      <div className="field">
        <label htmlFor="city">Miasto</label>
        <input
          id="city"
          name="city"
          type="text"
          data-testid="city-input"
          value={form.city}
          onChange={handleChange}
        />
      </div>

      <div className="field">
        <label htmlFor="postalCode">Kod pocztowy</label>
        <input
          id="postalCode"
          name="postalCode"
          type="text"
          placeholder="00-000"
          data-testid="postal-code-input"
          value={form.postalCode}
          onChange={handleChange}
        />
      </div>

      <fieldset className="field">
        <legend>Metoda płatności</legend>
        {(['CARD', 'BLIK', 'TRANSFER'] as const).map((method) => (
          <label key={method} className="radio-label">
            <input
              type="radio"
              name="paymentMethod"
              value={method}
              data-testid={`payment-${method}`}
              checked={form.paymentMethod === method}
              onChange={handleChange}
            />
            {method}
          </label>
        ))}
      </fieldset>

      <button
        type="submit"
        data-testid="submit-button"
        disabled={submitting}
      >
        {submitting ? 'Wysyłanie…' : 'Złóż zamówienie'}
      </button>
    </form>
  );
}
