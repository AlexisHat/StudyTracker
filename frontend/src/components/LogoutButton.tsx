import React from "react";
import { logout } from "../utils/auth";
import { useNavigate } from "react-router-dom";

const LogoutButton: React.FC = () => {
  const navigate = useNavigate();
  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <button
      onClick={handleLogout}
      className="btn btn-danger d-flex align-items-center gap-2"
    >
      <i className="fas fa-right-from-bracket"></i>
      <span>Logout</span>
    </button>
  );
};

export default LogoutButton;
