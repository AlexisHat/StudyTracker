import React, { useState } from "react";
import { LoginData, RegisterData } from "../types/auth.types";
import { login, register } from "../service/AuthService";
import { useNavigate } from "react-router-dom";
import { setToken } from "../utils/auth";
import FormLogicViolationAlert from "../components/FormLogicViolationAlert";

function Login() {
  const [mode, setMode] = useState<"login" | "register">("login");
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const isLogin = mode === "login";
  const navigate = useNavigate();

  const [loginData, setLoginData] = useState<LoginData>({
    username: "",
    password: "",
  });

  const [registerData, setRegisterData] = useState<RegisterData>({
    email: "",
    username: "",
    fullName: "",
    password: "",
  });

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorMessage(null);
    try {
      const response = await login(loginData);

      console.log("Login erfolgreich:", response.data);
      setToken(response.data);
      navigate("/");
    } catch (error: any) {
      console.error("Login-Fehler:", error);
      setErrorMessage(
        "Login fehlgeschlagen: Bitte Benutzername und Passwort überprüfen."
      );
    }
  };

  const handleRegister = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      const response = await register(registerData);
      console.log("Registrierung erfolgreich:", response.data);
      setToken(response.data);
      navigate("/");
    } catch (error: any) {
      console.error("Registrierungs-Fehler:", error);
      const backendMessage = error?.response?.data?.message;
      setErrorMessage(
        backendMessage ||
          "Registrierung fehlgeschlagen. Bitte versuche es erneut."
      );
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100 w-100 bg-light">
      <div
        className="bg-white p-4 rounded shadow-sm"
        style={{ maxWidth: "400px", width: "100%" }}
      >
        <div className="mb-3 d-flex justify-content-between">
          <button
            type="button"
            className={`btn ${isLogin ? "btn-primary" : "btn-light"} w-50 me-1`}
            onClick={() => {
              setMode("login");
              setErrorMessage(null);
            }}
          >
            Login
          </button>
          <button
            type="button"
            className={`btn ${isLogin ? "btn-light" : "btn-primary"} w-50 ms-1`}
            onClick={() => {
              setMode("register");
              setErrorMessage(null);
            }}
          >
            Registrierung
          </button>
        </div>

        <FormLogicViolationAlert message={errorMessage} />

        <h2 className="text-center mb-4">
          {isLogin ? "Login" : "Registrierung"}
        </h2>

        {isLogin && (
          <form onSubmit={handleLogin}>
            <div className="mb-3">
              <label htmlFor="username" className="form-label">
                Benutzername:
              </label>
              <input
                type="text"
                id="username"
                name="username"
                className="form-control"
                value={loginData.username}
                onChange={(e) =>
                  setLoginData({ ...loginData, username: e.target.value })
                }
              />
            </div>

            <div className="mb-3">
              <label htmlFor="password" className="form-label">
                Passwort:
              </label>
              <input
                type="password"
                id="password"
                name="password"
                className="form-control"
                value={loginData.password}
                onChange={(e) =>
                  setLoginData({ ...loginData, password: e.target.value })
                }
              />
            </div>

            <button type="submit" className="btn btn-success w-100">
              Einloggen
            </button>
          </form>
        )}

        {!isLogin && (
          <form onSubmit={handleRegister}>
            <div className="mb-3">
              <label htmlFor="email" className="form-label">
                E-Mail:
              </label>
              <input
                type="email"
                id="email"
                name="email"
                className="form-control"
                value={registerData.email}
                onChange={(e) =>
                  setRegisterData({ ...registerData, email: e.target.value })
                }
              />
            </div>

            <div className="mb-3">
              <label htmlFor="firstName" className="form-label">
                Vorname:
              </label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                className="form-control"
                value={registerData.fullName.split(" ")[0] || ""}
                onChange={(e) =>
                  setRegisterData({
                    ...registerData,
                    fullName: `${e.target.value} ${
                      registerData.fullName.split(" ")[1] || ""
                    }`,
                  })
                }
              />
            </div>

            <div className="mb-3">
              <label htmlFor="lastName" className="form-label">
                Nachname:
              </label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                className="form-control"
                value={registerData.fullName.split(" ")[1] || ""}
                onChange={(e) =>
                  setRegisterData({
                    ...registerData,
                    fullName: `${registerData.fullName.split(" ")[0] || ""} ${
                      e.target.value
                    }`,
                  })
                }
              />
            </div>

            <div className="mb-3">
              <label htmlFor="username" className="form-label">
                Benutzername:
              </label>
              <input
                type="text"
                id="username"
                name="username"
                className="form-control"
                value={registerData.username}
                onChange={(e) =>
                  setRegisterData({ ...registerData, username: e.target.value })
                }
              />
            </div>

            <div className="mb-3">
              <label htmlFor="password" className="form-label">
                Passwort:
              </label>
              <input
                type="password"
                id="password"
                name="password"
                className="form-control"
                value={registerData.password}
                onChange={(e) =>
                  setRegisterData({ ...registerData, password: e.target.value })
                }
              />
            </div>

            <button type="submit" className="btn btn-success w-100">
              Registrieren
            </button>
          </form>
        )}
      </div>
    </div>
  );
}

export default Login;
