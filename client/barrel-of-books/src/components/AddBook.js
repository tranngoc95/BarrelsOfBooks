import { useState, useEffect } from "react";
import {useHistory, Link} from "react-router-dom";

function AddBook() {
const [book,setBook] = useState({
    title: "",
    description: "",
    author: "",
    price: 0,
    quantity: 0,
    genres: [],
    stores: []
})
const[genres, setGenres] = useState([]);
const[stores, setStores] = useState([]);
const[errorList, setErrorList] = useState([]);
const history = useHistory();



const getGenres = () => {
    return fetch("http://localhost:8080/api/genre")
    .then((response) => response.json())
    .then((data) => setGenres(data))
    .catch((error) => console.log(error));
}

useEffect(getGenres,[]);

const getStores = () => {
  return fetch("http://localhost:8080/api/store")
  .then((response) => response.json())
  .then((data) => {
    data.forEach(store => store.quantity = 0);
    setStores(data);
  })
  .catch((error) => console.log(error));
}

useEffect(getStores,[]);


const handleChange = (event) => {
    const newBook = {...book};
    if(event.target.name === "genres") {
      newBook[event.target.name] = handleGenres(event);
      setBook(newBook);
      console.log(book.genres);
    } else if (event.target.name === "stores") {
      handleStores(event, newBook);
    } else if (event.target.name === "storeQuantity") {
      handleQuantity(event, newBook);
    } else {
      newBook[event.target.name] = event.target.value;
      setBook(newBook);
    }   
}

function handleGenres(event) {
  const newGenres = [...book.genres];
  const genreId = parseInt(event.target.value,10);
  const genre = genres.find((genre) => genre.genreId === genreId);
  if(event.target.checked) {
    newGenres.push(genre);
  } else {
    const genreIndex = newGenres.indexOf(genre);
    if(genreIndex >= 0) {
      newGenres.splice(genreIndex,1);
    }
  }
  return newGenres;
 }

 function handleStores(event, newBook) {
  const newStores = [...newBook.stores];
  const storeId = parseInt(event.target.value,10);
  if(event.target.checked) {
    const store = stores.find((store) => store.storeId === storeId);
    newStores.push(store);
    newBook.stores = newStores;
    setBook(newBook);
  } else {
    const storeIndex = newStores.findIndex(store => store.storeId === storeId);
    if(storeIndex >= 0) {
      newStores.splice(storeIndex,1);
      newBook.stores = newStores;
      setBook(newBook);
    }
  }
 }

 function handleQuantity(event, newBook) {
    const storeId = parseInt(event.target.id,10);
    const newStores = [...stores];
    const storeIndex = newStores.findIndex(store => store.storeId === storeId);
    newStores[storeIndex] = {...newStores[storeIndex], quantity: parseInt(event.target.value ,10)};
    setStores(newStores);

    const newBookStores = [...newBook.stores];
    const bookStoreIndex = newBookStores.findIndex(store => store.storeId === storeId);
    newBookStores[bookStoreIndex].quantity = parseInt(event.target.value ,10);
    newBook.stores = newBookStores;
    setBook(newBook);
 }






const handleSubmit = (event) => {
  event.preventDefault();
  const newBook = {...book};

  const init = {
      method: "POST",
      headers: {
          "Content-Type": "application/json"
      },
      body: JSON.stringify(newBook),
  };

  fetch("http://localhost:8080/api/book", init)
  .then((response) => {
      if (
          response.status === 201 ||
          response.status === 204 ||
          response.status === 400
      ) {
          return response.json();
      }
    return Promise.reject("Something else went wrong");
  })
  .then((data) => {
      if(data.bookId) {
        handleStoreBook(data.bookId);
        
      } else {
        setErrorList(data);
      }
  })
  .catch((error) => console.log(error));

}



// function getGenresFromList() {
//   const list = [];

//   book.genres.map((g) => {

//      fetch(`http://localhost:8080/api/genre/${g}`)
//      .then(response => response.json())
//      .then(data => list.push(data))
//      .catch(error => console.log(error));

//  }
// )
// return list;
// }

function handleGenreBook(bookId) {

  book.genres.map((genre) => {
    console.log(genre);
   const genreBook = { bookId, genre };

   const init = {
     method: "POST",
     headers: {
         "Content-Type": "application/json"
     },
     body: JSON.stringify(genreBook),
 };
   fetch("http://localhost:8080/api/genre-book",init)
   .then((response) => response.json())
   .then((data) => {
       if(data.bookId) {
         history.push("/books");
       }
   })
   .catch((error) => console.log(error));
 })
}




function handleStoreBook(bookId) {
  book.stores.map((s) => {
    const storeBook = { bookId, store: s, quantity: s.quantity };

    const init = {
      method: "POST",
      headers: {
          "Content-Type": "application/json"
      },
      body: JSON.stringify(storeBook),
  };
    fetch("http://localhost:8080/api/store-book",init)
    .then((response) => response.json())
    .then((data) => {
        if(data.bookId) {
          handleGenreBook(data.bookId);
        }
    })
    .catch((error) => console.log(error));
  })
}


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
        <h3>Genres</h3>
        {genres.map((g) => (
          <div key={g.genreId}>
            <input type="checkbox" value={g.genreId} id={g.name} name="genres"
              checked={book.genres.includes(g)} onChange={handleChange} />
            <label htmlFor={g.name}>{g.name}</label>
          </div>
        ))}
      </div>


      <div>
        <h3>Stores</h3>
        {stores.map((s) => (
          <div>
          <div key={s.storeId}>
            <input type="checkbox" value={s.storeId} id={s.address} name="stores"
              checked={book.stores.find((store) => store.storeId === s.storeId) !== undefined} onChange={handleChange} />
            <label htmlFor={s.address}>{s.address}</label> 
          </div>
          <div>
            <label htmlFor={s.storeId}>Quantity</label>
            <input type="number" min="1" id={s.storeId} name="storeQuantity" value={s.quantity} onChange={handleChange} 
                disabled = {book.stores.find((store) => store.storeId === s.storeId) === undefined}/>
          </div>
          </div>
        ))}
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