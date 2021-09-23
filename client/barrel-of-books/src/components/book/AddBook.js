import { useState, useEffect,useContext } from "react";
import {useHistory, Link} from "react-router-dom";
import ErrorMessages from "../ErrorMessages";
import AuthContext from '../../AuthContext';
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
const[selectedGenres, setSelectedGenres] = useState([]);
const[stores, setStores] = useState([]);
const[errorList, setErrorList] = useState([]);
const history = useHistory();
const auth = useContext(AuthContext);



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
      handleGenres(event);
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
  const newGenres = [...selectedGenres];
  const genreId = parseInt(event.target.value,10);
  const genre = genres.find((genre) => genre.genreId === genreId);
  if(event.target.checked) {
    newGenres.push(genre);
    setSelectedGenres(newGenres);
  } else {
    const genreIndex = newGenres.indexOf(genre);
    if(genreIndex >= 0) {
      newGenres.splice(genreIndex,1);
      setSelectedGenres(newGenres);
    }
  }
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
  newBook.genres = selectedGenres;

  const init = {
      method: "POST",
      headers: {
          "Content-Type": "application/json",
          'Authorization': `Bearer ${auth.user.token}`
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



function handleGenreBook(bookId) {
 
  selectedGenres.map((genre) => {
   const genreBook = { bookId, genre };


   const init = {
     method: "POST",
     headers: {
         "Content-Type": "application/json",
         'Authorization': `Bearer ${auth.user.token}`
     },
     body: JSON.stringify(genreBook),
 };
   fetch("http://localhost:8080/api/genre-book",init)
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
         history.push("/books");
       } else {
        setErrorList(data);
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
          "Content-Type": "application/json",
          'Authorization': `Bearer ${auth.user.token}`
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
    <div className="seashell-bg">
    <h1>Add New Book</h1>
    <ErrorMessages errorList={errorList} />
    <form className="ui small form" onSubmit = {handleSubmit}>
      <div class="ui stacked segment">
      <div className="field">
        <label htmlFor="title">Title</label>
        <input type="text" id="title" name="title" value={book.title} onChange={handleChange}/>
      </div>
      <div className="field">
        <label htmlFor="description">Description</label>
        <textarea type="text" id="description" name="description" value={book.description} onChange={handleChange}/>
      </div>

      <div className="field">
        <label htmlFor="author">Author</label>
        <input type="text" id="author" name="author" value={book.author} onChange={handleChange}/>
      </div>

      <div className="field">
        <label htmlFor="price">Price $</label>
        <input type="number" min="1" step="any" id="price" name="price" value={book.price} onChange={handleChange}/>
      </div>
      <div className="field">
        <label htmlFor="quantity">Quantity</label>
        <input type="number" min="1" id="quantity" name="quantity" value={book.quantity} onChange={handleChange}/>
      </div>


      <div>
        <h3 className="form-title" >Genres</h3>
        {genres.map((g) => (
          <div className="ui checkbox" key={g.genreId}>
            <input type="checkbox" value={g.genreId} id={g.name} name="genres"
              checked={selectedGenres.includes(g)} onChange={handleChange} />
            <label htmlFor={g.name}>{g.name}</label>
          </div>
        ))}
      </div>


      <div>
        <h3 className="form-title" >Stores</h3>
        {stores.map((s) => (
          <div class="ui stacked segment">
          <div className="ui checkbox check-store" key={s.storeId}>
            <input type="checkbox" value={s.storeId} id={s.address} name="stores"
              checked={book.stores.find((store) => store.storeId === s.storeId) !== undefined} onChange={handleChange} />
            <label htmlFor={s.address}>{s.address}</label> 
          </div>
          <div className="field">
            <label className="store-quantity" htmlFor={s.storeId}>Quantity</label>
            <input type="number" min="0" id={s.storeId} name="storeQuantity" value={s.quantity} onChange={handleChange} 
                disabled = {book.stores.find((store) => store.storeId === s.storeId) === undefined}/>
          </div>
          </div>
        ))}
      </div>
      <div className="ui buttons">
						<button className="ui positive button active" type="submit">Submit</button>
						<div class="or"></div>
						<Link className="ui button" type="button" to='/books'>Cancel</Link>
					</div>
      </div>
    </form>
  </div>
)




}

export default AddBook;