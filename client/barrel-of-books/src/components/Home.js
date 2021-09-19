import {Link} from "react-router-dom";

function Home() {
return (
    <div>
    <h1 className="mt-5 mb-5 main-title">Barrel Of Books</h1>
      <div className="col">
        <Link  to="/books">Books</Link>      
      </div>
      <div className="col">
      <Link  to="/stores">Stores</Link> 
      </div>
      <div className="col">
      <Link  to="/genres">Genres</Link> 
      </div>
      <div className="col">
      <Link  to="/cart">Cart</Link> 
      </div>
    </div>
)
}


export default Home;