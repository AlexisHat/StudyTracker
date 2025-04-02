import { Link } from "react-router-dom";

function Home() {
  return (
    <>
      <h1>Willkommen zum Study-Tracker</h1>
      <Link to="/login">Zum Login</Link>
    </>
  );
}

export default Home;
