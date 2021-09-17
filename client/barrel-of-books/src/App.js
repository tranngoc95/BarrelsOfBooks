
import './App.css';

function App() {
  return (
    <Router>
        <div className="App">
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
          </Switch>
        </div>
      </Router>
  );
}

export default App;
