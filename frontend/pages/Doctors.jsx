import React from "react";

const doctors = [
  { id: 1, name: "Dr. Alice Uwase", specialty: "Cardiology", phone: "0788 000 001" },
  { id: 2, name: "Dr. Jean Mugisha", specialty: "Pediatrics", phone: "0788 000 002" },
  { id: 3, name: "Dr. Grace Ineza", specialty: "Dermatology", phone: "0788 000 003" },
];

export default function Doctors() {
  return (
    <main style={{ padding: "1rem" }}>
      <h1>Our Doctors</h1>
      <p>Meet our team of {doctors.length} experienced doctors, ready to help you.</p>
      <div style={{ display: "flex", flexWrap: "wrap", gap: "1rem" }}>
        {doctors.map((doctor) => (
          <div key={doctor.id} style={{ border: "1px solid #ddd", borderRadius: "8px", padding: "1rem", minWidth: "200px" }}>
            <h3 style={{ margin: "0 0 0.5rem" }}>{doctor.name}</h3>
            <p style={{ margin: 0 }}>🩺 {doctor.specialty}</p>
            <p style={{ margin: 0, color: "#666" }}>📞 {doctor.phone}</p>
          </div>
        ))}
      </div>
    </main>
  );
}
