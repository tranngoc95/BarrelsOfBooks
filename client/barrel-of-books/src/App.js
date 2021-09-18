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
import Cart from "./components/order/Cart";
import Orders from "./components/order/Orders";
import CancelOrder from "./components/order/CancelOrder";
import CancelItem from "./components/order/CancelItem";
import Confirmation from "./components/order/Confirmation";

const UserRoutes = [
  { path: "/cart", component: Cart },
  { path: "/orders", component: Orders },
  { path: "/stores", component: Stores },
  { path: "/orders/cancel/:id", component: CancelOrder},
  { path: "/orders/cancel/item/:id", component: CancelItem},
  { path: "/orders/confirmation/:id", component: Confirmation}
]

const AdminRoutes = [
  { path: "/stores/edits", component: EditStore },
]

function App() {
  const user = "placeholder";
  return (
    <Router>
      <div className="App">
        <Home />
        
        <Switch>
          <Route exact path="/">
            <Home />
          </Route>
          {AdminRoutes.map(each => (
            <Route key={each.path} exact path={each.path}>
              {user ?
                <each.component />
                :
                <Redirect to={{
                  pathname: '/login',
                  state: { nextpath: each.path }
                }} />
              }
            </Route>
          ))}

          {UserRoutes.map(each => (
            <Route key={each.path} exact path={each.path}>
              {user ?
                <each.component />
                :
                <Redirect to={{
                  pathname: '/login',
                  state: { nextpath: each.path }
                }} />
              }
            </Route>
          ))}

        </Switch>
      </div>
    </Router>
  );
}

export default App;
