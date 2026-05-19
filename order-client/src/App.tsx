import { useState } from 'react';
import type { OrderResponse } from './types';
import OrderForm from './components/OrderForm';
import ConfirmationView from './components/ConfirmationView';
import './styles/main.css';

export default function App() {
  const [confirmation, setConfirmation] = useState<OrderResponse | null>(null);

  return (
    <div className="app">
      {confirmation ? (
        <ConfirmationView
          response={confirmation}
          onReset={() => setConfirmation(null)}
        />
      ) : (
        <OrderForm onSuccess={setConfirmation} />
      )}
    </div>
  );
}
