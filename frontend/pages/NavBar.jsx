import React from "react";
import { NavLink } from "react-router-dom";

const linkStyle = ({ isActive }) => ({
  marginRight: "1rem",
  color: "#1d4ed8",
  fontWeight: isActive ? "bold" : "normal",
  textDecoration: isActive ? "underline" : "none",
});

export default function NavBar() {
  return (
    <nav style={{ display: "flex", alignItems: "center", gap: "2rem", padding: "1rem", borderBottom: "1px solid #ddd" }}>
      <div>
        <strong>🏥 Clinic</strong>
        <div style={{ fontSize: "0.8rem", color: "#666" }}>Caring for you every day</div>
      </div>
      <div>
        <NavLink to="/" end style={linkStyle}>Offices</NavLink>
        <NavLink to="/doctors" style={linkStyle}>Doctors</NavLink>
      </div>
    </nav>
  );
}
