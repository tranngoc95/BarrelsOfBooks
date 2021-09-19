import { useState, useEffect } from "react";
import {useHistory, Link} from "react-router-dom";

function AddBook() {
const [book,setBook] = useState({
    title: "",
    description: "",
    author: "",
    price: 0,
    quantity: 0,
    genres: []
})
const[genres, setGenres] = useState([]);
const[bookGenres, setBookGenres] = useState([]);
const[errorList, setErrorList] = useState([]);
const history = useHistory();

const getGenres = () => {
    return fetch("http://localhost:8080/api/genre")
    .then((response) => response.json())
    .then((data) => setGenres(data))
    .catch((error) => console.log(error));
}

useEffect(getGenres,[]);








const handleChange = (event) => {
    const newBook = {...book};
    newBook[event.target.name] = event.target.value;
    setBook(newBook);
}

const handleSubmit = () => {
console.log(bookGenres);
}

//  function handleSelectChange(event) {
//     let value = Array.from(
//         event.target.selectedOptions,
//         (option) => option.value
//       );
//       this.setBookGenres({
//         options: value,
//       });
//  }

return (
    <div>
    <h1>add new book</h1>
{errorList.length > 0 ? (
  <div>
      {errorList.map((error) => (
      <li key={error}>{error}</li>
  ))}
</div>
) : null}
    <form onSubmit = {handleSubmit}>
      <div>
        <label htmlFor="title">Title</label>
        <input type="text" id="title" name="title" value={book.title} onChange={handleChange}/>
      </div>
      <div>
        <label htmlFor="description">Description</label>
        <textarea type="text" id="description" name="description" value={book.description} onChange={handleChange}/>
      </div>

      <div>
        <label htmlFor="author">Author</label>
        <input type="text" id="author" name="author" value={book.author} onChange={handleChange}/>
      </div>

      <div>
        <label htmlFor="price">Price $</label>
        <input type="number" min="1" step="any" id="price" name="price" value={book.price} onChange={handleChange}/>
      </div>
      <div>
        <label htmlFor="quantity">Quantity</label>
        <input type="number" min="1" id="quantity" name="quantity" value={book.quantity} onChange={handleChange}/>
      </div>
      <div>
        <label htmlFor="genres">Choose genres:</label>
        <select  value={this.bookGenres.options} name="genres" id="genres" onChange={this.handleSelectChange} multiple>
            {genres.map((g) => (
                <option value={g.name} key={g.name}>{g.name}</option>
            ))}
        </select>



      </div>

      <div>
          <button type="submit">Submit</button>
      </div>
    </form>
    <Link className="btn btn-warning" to="/books">Go Back</Link>
  </div>
)




}

export default AddBook;