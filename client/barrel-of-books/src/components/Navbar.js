import { Link } from "react-router-dom";
import AuthContext from '../AuthContext';
import { useContext } from 'react';

function Navbar() {

    const auth = useContext(AuthContext);

    return (
        <>
            <div class="ui secondary menu">
                <Link className="item" to="/">Barrel of Books</Link>
                <Link className="item" to="/books">Books</Link>
                <Link className="item" to="/stores">Stores</Link>
                <Link className="item" to="/genres">Genres</Link>
                {auth.user && <Link className="item" to="/orders">My Orders</Link>}
                {auth.user && auth.user.hasRole('MANAGER') && <Link className="item" to="/allorders">All Orders</Link>}
                <div class="right menu">
                    {!auth.user && (
                        <>
                            <Link className="ui item" to="/login">Login</Link>
                            <Link className="ui item" to="/register">Register</Link>
                        </>
                    )}
                    {auth.user &&
                        <>
                            <span className="ui item"><i>Hello {auth.user.username}!</i></span>
                            <Link className="item" to="/cart">Cart</Link>
                            <Link className="ui item" to="/login" onClick={() => auth.logout()} >Logout</Link>
                        </>
                    }
                </div>
            </div>
        </>
    )
}

export default Navbar;