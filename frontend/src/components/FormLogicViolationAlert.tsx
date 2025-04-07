type Props = {
  message: string | null;
};

const FormLogicViolationAlert: React.FC<Props> = ({ message }) => {
  if (!message) return null;

  return (
    <div className="alert alert-info mt-2" role="alert">
      {message}
    </div>
  );
};

export default FormLogicViolationAlert;
