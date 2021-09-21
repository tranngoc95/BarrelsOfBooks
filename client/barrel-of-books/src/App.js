import * as React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import './App.css';
import Home from "./components/Home";
import Stores from "./components/Stores";
import EditStore from "./components/EditStore";
import AddStore from "./components/AddStore";
import Books from "./components/Books";
import AddBook from "./components/AddBook";


function App() {
  return (
    <Router>
        <div className="App">
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route exact path="/stores">
              <Stores />
            </Route>
            <Route exact path ="/stores/edit/:id">
              <EditStore />
            </Route>
            <Route exact path = "/stores/add">
              <AddStore />
            </Route>
            <Route exact path = "/books">
              <Books />
            </Route>
            <Route exact path = "/books/add">
              <AddBook />
            </Route>
          </Switch>
        </div>
      </Router>
  );
}

export default App;
