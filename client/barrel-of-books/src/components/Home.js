import { useState } from "react";
import { Link } from "react-router-dom";

function Home() {
  const [phrase, setPhrase] = useState("");

  return (
    <div className="home-page">
      <h1 className="main-title">Barrel Of Books</h1>
      <div class="ui icon input">
        <input type="text" placeholder="Search by Title, Author, or Keyword" onChange={(event) => setPhrase(event.target.value)} />
        <Link to={`/books/search/${phrase}`} className="ui button"><i class="search link icon"></i></Link>
      </div>
    </div>
  )
}


export default Home;