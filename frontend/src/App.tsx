import TicketList from "./components/TicketList";
import TicketForm from "./components/TicketForm";

function App() {
  return (
      <main>
        <h1>Ticket Prioritization System</h1>
          <TicketForm />
          <TicketList />
      </main>
  );
}

export default App;