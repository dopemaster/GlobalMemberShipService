package edu.illinois.cs425.mp2;



public class JoinMessage extends Message {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public JoinMessage(String messageType) {
		super(messageType);
	}

	public JoinMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}



	@Override
	public void processMessage() {
		System.out.println("Processign message");
		new ServiceThread(this) {


			@Override
			public void run() {
				try {
					ProcessorThread.getServer().getLogger().info("Servicing Join request of node"+getMessage().getSourceNode().getHostAddress());
					// Pre condition: source node must be populated with the node joined/left/failed
					((JoinMessage) getMessage()).mergeIntoMemberList();

					// should change this step (TODO: master may be detected as
					// failure)
					MemberNode oldNeighbourNode = ProcessorThread.getServer().getNeighborNode();
					ProcessorThread.getServer().setNeighborNode(
							getMessage().getSourceNode());
					//ProcessorThread.toStartHeartBeating=false;
					//ProcessorThread.getServer().getTimer().stop();
					Message ackMessage = new JoinAckMessage(ProcessorThread.getServer().getNode(), null, null);

					((JoinAckMessage)ackMessage).setNeighbourNode(oldNeighbourNode);
					((JoinAckMessage)ackMessage).setGlobalList(ProcessorThread.getServer().getGlobalList());
					ProcessorThread.getServer().getLogger().info("Neighbour node in Join Ack message is: "+ ProcessorThread.getServer().getNeighborNode());
					ackMessage.setMulticastGroup(ProcessorThread
							.getMulticastServer().getMulticastGroup());
					ackMessage.setMulticastPort(ProcessorThread
							.getMulticastServer().getMulticastPort());



					ProcessorThread.getServer().sendMessage(ackMessage, getMessage().getSourceNode());
					// ProcessorThread.getServer().setSendHeartBeat(true);
					// ensure reliability in 5 seconds (TODO)
					// send back the multi-cast group, port number neighbour to
					// new node

				} catch (Exception e) {

					ProcessorThread.getServer().getLogger().info("Processing join failed of node"+getMessage().getSourceNode().getHostAddress());
                    System.out.println("Processing join failed");
				}
			}
		}.start();

	}
}
