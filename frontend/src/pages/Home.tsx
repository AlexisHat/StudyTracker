import { Link } from "react-router-dom";
import LogoutButton from "../components/LogoutButton"; // Pfad ggf. anpassen
import { isLoggedIn } from "../utils/auth";

function Home() {
  return (
    <div className="container mt-5">
      <h1 className="mb-4">Willkommen zum Study-Tracker</h1>

      <div className="mb-3">
        <Link to="/login" className="btn btn-primary me-3">
          Zum Login
        </Link>
        {isLoggedIn() && <LogoutButton />}
        <Link to="/create-session" className="btn btn-primary me-3">
          Eine neue Session
        </Link>
        <Link to="/sessions/overview" className="btn btn-primary me-3">
          Overview
        </Link>
      </div>
    </div>
  );
}

export default Home;
