interface Props {
  errors: Record<string, string> | null;
  unavailableMsg: string | null;
}

export default function ErrorSummary({ errors, unavailableMsg }: Props) {
  if (!errors && !unavailableMsg) return null;

  return (
    <div data-testid="error-summary" className="error-summary">
      <ul>
        {errors &&
          Object.entries(errors).map(([field, msg]) => (
            <li key={field} data-testid={`error-${field}`}>
              {msg}
            </li>
          ))}
        {unavailableMsg && (
          <li data-testid="error-unavailable">{unavailableMsg}</li>
        )}
      </ul>
    </div>
  );
}
