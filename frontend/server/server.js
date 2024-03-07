const jsonServer = require('json-server');
const middleware = jsonServer.defaults();
const server = jsonServer.create();

server.use(middleware)
server.use(jsonServer.bodyParser);

const projects = require('../server/data/projects');

server.get('/projects', (req, res, next) => {
  res.status(200).send(projects.getProjects);
})

server.listen(3000, () => {
  console.log('JSON server listening on port 3000');
})
