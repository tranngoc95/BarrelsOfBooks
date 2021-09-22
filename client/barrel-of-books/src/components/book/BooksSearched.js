import { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import AuthContext from '../../AuthContext';
import BooksTable from "./BooksTable";

function BooksSearched() {

    const [books, setBooks] = useState([]);
    const auth = useContext(AuthContext);
    const { phrase } = useParams();

    const getList = () => {
        fetch(`http://localhost:8080/api/book/search/${phrase}`)
            .then((response) => response.json())
            .then((data) => setBooks(data))
            .catch((error) => console.log(error));
    }

    useEffect(getList, []);

    return (
        <div className="home-page">
            <h2 className="sub-title">Books</h2>
            <BooksTable books={books} auth={auth} getList={getList} />
        </div>
    )

}

export default BooksSearched;