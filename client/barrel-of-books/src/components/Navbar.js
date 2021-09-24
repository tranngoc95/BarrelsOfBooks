import { Link } from "react-router-dom";
import AuthContext from '../AuthContext';
import { useContext } from 'react';

function Navbar() {

    const auth = useContext(AuthContext);

    return (
        <div className="seashell-bg">
            <div className="ui secondary menu">
                <Link className="item" to="/"><b><i>Barrel of Books</i></b></Link>
                <Link className="item" to="/books">Books</Link>
                <Link className="item" to="/stores">Stores</Link>
                <Link className="item" to="/genres">Genres</Link>
                {auth.user && <Link className="item" to="/orders">My Orders</Link>}
                {auth.user && auth.user.hasRole('MANAGER') && <Link className="item" to="/allorders">All Orders</Link>}
                <div className="right menu">
                    {!auth.user && (
                        <>
                            <Link className="ui item" to="/login">Login</Link>
                            <Link className="ui item" to="/register">Register</Link>
                        </>
                    )}
                    {auth.user &&
                        <>
                            <span className="ui item"><i>Hello {auth.user.username}!</i></span>
                            <div className="ui item vertical animated button" tabindex="0">
                                <Link className="visible content" to="/cart"><i class="shopping cart icon"></i></Link>
                                <Link className="hidden content" to="/cart">Cart</Link>
                            </div>

                            <Link className="ui item" to="/login" onClick={() => auth.logout()} >Logout</Link>
                        </>
                    }
                </div>
            </div>
        </div>
    )
}

export default Navbar;