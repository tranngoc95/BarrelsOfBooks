import {Link} from "react-router-dom";

function Home() {
return (
    <div>
    <h1 className="mt-5 mb-5 main-title">Barrel Of Books</h1>
      <div className="col">
        <Link  to="/api/books">Books</Link>      
      </div>
      <div className="col">
      <Link  to="/api/stores">Stores</Link> 
      </div>
      <div className="col">
      <Link  to="/api/genres">Genres</Link> 
      </div>
      <div className="col">
      <Link  to="/api/cart">Cart</Link> 
      </div>
    </div>
)
}


export default Home;