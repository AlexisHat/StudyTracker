import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import CreateSession from "./pages/CreateSession";
import SessionOverview from "./pages/SessionOverview";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/create-session" element={<CreateSession />} />
        <Route path="/sessions/overview" element={<SessionOverview />} />
      </Routes>
    </Router>
  );
}

export default App;
