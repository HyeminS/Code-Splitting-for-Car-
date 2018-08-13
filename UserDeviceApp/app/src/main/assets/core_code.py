import logging

DEFAULT_MASTER_PORT=11311

class Master(object):
    def __init__(self, port=DEFAULT_MASTER_PORT, num_workers=rosmaster.master_api.NUM_WORKERS):
        self.port = port
        self.num_workers = num_workers
    def start(self):
        """
        Start the ROS Master.
        """
        self.handler = None
        self.master_node = None
        self.uri = None
        handler = rosmaster.master_api.ROSMasterHandler(self.num_workers)
        master_node = rosgraph.xmlrpc.XmlRpcNode(self.port, handler)
        master_node.start()
        while not master_node.uri:
            time.sleep(0.0001)
        self.handler = handler
        self.master_node = master_node
        self.uri = master_node.uri
        logging.getLogger('rosmaster.master').info("Master initialized: port[%s], uri[%s]", self.port, self.uri)
    def ok(self):
        if self.master_node is not None:
            return self.master_node.handler._ok()
        else:
            return False
    def stop(self):
        if self.master_node is not None:
            self.master_node.shutdown('Master.stop')
            self.master_node = None


