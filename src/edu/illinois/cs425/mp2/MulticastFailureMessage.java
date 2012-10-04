package edu.illinois.cs425.mp2;

public class MulticastFailureMessage extends MulticastMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MulticastFailureMessage(String messageType) {
		super(messageType);
		// TODO Auto-generated constructor stub
	}
	
	public void processMessage()
	{
		new ServiceThread(this) {
			@Override
			public void run() {
				try {
					
						
						
						
					if (mergeIntoMemberList()) {
						Message message = getNewRelayMessage(ProcessorThread.getServer().getNode(), getMessage().getSourceNode(), getMessage().getAlteredNode());
						ProcessorThread.getServer().sendMessage(message, ProcessorThread.getServer().getNeighborNode());
						if(getMessage().getAlteredNode().compareTo(ProcessorThread.getServer().getNeighborNode()))
						{
							ProcessorThread.getServer().setNeighborNode(getMessage().getAlteredNode());
						}
					
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	@Override
	public RelayFailureMessage getNewRelayMessage(MemberNode sourceNode, MemberNode centralNode,
			MemberNode alteredNode) {
		// TODO Auto-generated method stub
		return new RelayFailureMessage(sourceNode, centralNode, alteredNode);
	}

	public MulticastFailureMessage(MemberNode sourceNode, MemberNode centralNode, MemberNode alteredNode) {
		super(sourceNode, centralNode, alteredNode);
	}
	
	
	
}
