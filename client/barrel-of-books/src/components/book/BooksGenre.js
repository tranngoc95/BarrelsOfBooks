import { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import AuthContext from '../../AuthContext';
import BooksTable from "./BooksTable";

function BooksGenre() {

    const [books, setBooks] = useState([]);
    const auth = useContext(AuthContext);
    const { name } = useParams();

    const getList = () => {
        fetch(`http://localhost:8080/api/book/genre/${name}`)
            .then((response) => response.json())
            .then((data) => setBooks(data))
            .catch((error) => console.log(error));
    }

    useEffect(getList, [name]);

    return (
        <div className="home-page">
            <h2 className="sub-title">{name} Books</h2>
            <BooksTable books={books} auth={auth} getList={getList} />
        </div>
    )
}

export default BooksGenre;