import * as React from "react";
import { BrowserRouter as Router, Switch, Route, Redirect } from "react-router-dom";

import './App.css';
import AuthContext from "./AuthContext";
import Home from "./components/Home";
import Stores from "./components/Stores";
import EditStore from "./components/EditStore";
import Cart from "./components/order/Cart";
import Orders from "./components/order/Orders";
import CancelOrder from "./components/order/CancelOrder";
import CancelItem from "./components/order/CancelItem";
import Confirmation from "./components/order/Confirmation";
import Genres from "./components/genre/Genres";
import AddGenre from "./components/genre/AddGenre";
import EditGenre from "./components/genre/EditGenre";
import AllOrders from "./components/order/AllOrders";

const UserRoutes = [
  { path: "/cart", component: Cart },
  { path: "/orders", component: Orders },
  { path: "/stores", component: Stores },
  { path: "/orders/cancel/:id", component: CancelOrder },
  { path: "/orders/cancel/item/:id", component: CancelItem },
  { path: "/orders/confirmation/:id", component: Confirmation },
  { path: "/genres", component: Genres },
  { path: "/genres/add", component: AddGenre },
  { path: "/genres/edit/:id", component: EditGenre },
]

const ManagerRoutes = [
  { path: "/allorders", component: AllOrders },
]

const AdminRoutes = [
  { path: "/stores/edits", component: EditStore },

]

function App() {
  const user = { token: "placeholder" };
  const auth = { user }
  return (
    <AuthContext.Provider value={auth}>
      <Router>
        <div className="App">
          <Home />

          <Switch>
            <Route exact path="/">
              <Home />
            </Route>

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

            {ManagerRoutes.map(each => (
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

          </Switch>
        </div>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
