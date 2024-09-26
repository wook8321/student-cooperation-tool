import React, {useEffect, useState} from "react";
import axios from "axios";

const domain = "http://localhost:8080"

function App() {
  const [hello, setHello] = useState('');

  useEffect(() => {
    axios.get(domain + '/api/test')
        .then(res => {
          setHello(res.data);
        })
        .catch(error =>{
            alert(JSON.stringify(error))
        })
  }, []);
  return (
      <div className="App">
        백엔드 데이터 : {hello}
      </div>
  );
}

export default App;
